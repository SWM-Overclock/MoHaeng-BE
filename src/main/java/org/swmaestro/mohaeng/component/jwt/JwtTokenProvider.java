package org.swmaestro.mohaeng.component.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;
import org.springframework.transaction.annotation.Transactional;
import org.swmaestro.mohaeng.util.exception.InvalidRefreshTokenException;
import org.swmaestro.mohaeng.util.exception.NotExpiredTokenException;
import org.swmaestro.mohaeng.util.exception.RefreshTokenMismatchException;
import org.swmaestro.mohaeng.service.auth.AuthTokenService;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Random;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JwtTokenProvider {

    private final AuthTokenService authTokenService;

    @Value("${jwt.access-token.expire-length}")
    private long accessTokenValidityInMilliseconds;

    @Value("${jwt.refresh-token.expire-length}")
    private long refreshTokenValidityInMilliseconds;

    @Value("${jwt.token.secret-key}")
    private String secretKey;

    public String createAccessToken(String userEmail) {
        return createToken(userEmail, accessTokenValidityInMilliseconds);
    }

    public String createRefreshToken(String userEmail) {
        byte[] array = new byte[7];
        new Random().nextBytes(array);
        String generatedString = new String(array, StandardCharsets.UTF_8);
        String refreshToken = createToken(generatedString, refreshTokenValidityInMilliseconds);

        authTokenService.setDataWithExpiration(userEmail, refreshToken, refreshTokenValidityInMilliseconds);
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
        Claims claims = getTokenClaims(token);
        return claims.getSubject();
    }

    public Claims getTokenClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Claims getExpiredTokenClaims(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
        return null;
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

    public String reissueAccessToken(String accessToken, String refreshToken) {
        Claims claims = getExpiredTokenClaims(accessToken);
        if (claims == null) {
            throw new NotExpiredTokenException("Token is not expired yet.");
        }

        String userEmail = claims.getSubject();
        if (!validateToken(refreshToken)) {
            throw new InvalidRefreshTokenException("Invalid refresh token.");
        }

        String storedRefreshToken = authTokenService.getData(userEmail);
        if (!refreshToken.equals(storedRefreshToken)) {
            throw new RefreshTokenMismatchException("Refresh token mismatch.");
        }
        return createAccessToken(userEmail);
    }
}
