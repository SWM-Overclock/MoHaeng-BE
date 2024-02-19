package org.swmaestro.mohaeng.exception.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 인증 실패 처리를 위한 엔트리 포인트 클래스
 *
 * Spring Security의 {@link AuthenticationEntryPoint}를 구현하여,
 * REST API 호출 시 인증 실패(예: 유호하지 않은 인증 토큰)가 발생했을 때,
 * 클라이언트에게 HTTP 401 Unauthorized 응답을 반환한다.
 *
 * @implSpec 인증 실패 시, 요청 URI와 실패 원인을 로그로 기록하고, HTTP 401 상태 코드를 반환합니다.
 */
@Slf4j
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info("Authentication failure - Error: {}, Method: {}, URI: {}",
                authException.getMessage(),
                request.getMethod(),
                request.getRequestURI());

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }
}
