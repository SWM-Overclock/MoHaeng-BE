package org.swmaestro.mohaeng.util.exception.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 접근 거부 처리를 위한 핸들러 클래스
 *
 * Spring Security의 {@link AccessDeniedHandler}를 구현하여,
 * REST API 호출 시 접근 거부(예: 권한 없음)가 발생했을 때,
 * 클라이언트에게 HTTP 403 Forbidden 응답을 반환한다.
 *
 * @implSpec 접근 거부 시, 요청 URI와 실패 원인을 로그로 기록하고, HTTP 403 상태 코드를 반환합니다.
 */
@RequiredArgsConstructor
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        handlerExceptionResolver.resolveException(request, response, null, accessDeniedException);
    }
}
