package org.swmaestro.mohaeng.component.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;
import org.springframework.transaction.annotation.Transactional;
import org.swmaestro.mohaeng.domain.user.auth.CustomUserDetails;
import org.swmaestro.mohaeng.service.RedisService;
import org.swmaestro.mohaeng.service.auth.CustomUserDetailsService;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Random;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JwtTokenProvider {

    private final RedisService redisService;

    @Value("${jwt.access-token.expire-length}")
    private long accessTokenValidityInMilliseconds;

    @Value("${jwt.refresh-token.expire-length}")
    private long refreshTokenValidityInMilliseconds;

    @Value("${jwt.token.secret-key}")
    private String secretKey;

    public String createAccessToken(String payload) {
        return createToken(payload, accessTokenValidityInMilliseconds);
    }

    public String createRefreshToken(String userEmail) {
        byte[] array = new byte[7];
        new Random().nextBytes(array);
        String generatedString = new String(array, StandardCharsets.UTF_8);
        String refreshToken = createToken(generatedString, refreshTokenValidityInMilliseconds);

        redisService.setDataWithExpiration(refreshToken, userEmail, refreshTokenValidityInMilliseconds);
        return refreshToken;
    }

    public String createToken(String payload, long expireLength) {
        Claims claims = Jwts.claims().setSubject(payload);
        Date now = new Date();
        Date validity = new Date(now.getTime() + expireLength);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();
    }

    public String getPayload(String token){
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        } catch (JwtException e){
            throw new RuntimeException("유효하지 않은 토큰 입니다");
        }
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException exception) {
            return false;
        }
    }

    public String reissueAccessToken(String refreshToken) {
        if (!validateToken(refreshToken)) {
            throw new RuntimeException("유효하지 않은 리프레쉬 토큰 입니다");
        }

        String userEmail = redisService.getData(refreshToken);
        log.info("userEmail: {}", userEmail);
        if (!redisService.validateToken(userEmail, refreshToken)) {
            throw new RuntimeException("유효하지 않은 리프레쉬 토큰 입니다");
        }
        return createAccessToken(userEmail);
    }
}
