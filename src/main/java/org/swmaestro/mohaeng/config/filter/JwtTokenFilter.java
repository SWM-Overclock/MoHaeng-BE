package org.swmaestro.mohaeng.config.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.swmaestro.mohaeng.component.jwt.JwtTokenProvider;
import org.swmaestro.mohaeng.domain.user.auth.CustomUserDetails;
import org.swmaestro.mohaeng.service.auth.CustomUserDetailsService;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String PREFIX_TOKEN = "Bearer ";


    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = extractToken(request);
        log.info("token: {}", token);
        if (token != null && jwtTokenProvider.validateToken(token)) {
            Authentication auth = getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
            log.info("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", auth.getName(), request.getRequestURI());
        }
        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HEADER_AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith(PREFIX_TOKEN)) {
            return bearerToken.substring(PREFIX_TOKEN.length());
        }
        return null;
    }

    private Authentication getAuthentication(String token) {
        String username = jwtTokenProvider.getPayload(token);
        // UserDetails 로드
        CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
