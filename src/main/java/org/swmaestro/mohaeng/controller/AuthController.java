package org.swmaestro.mohaeng.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.swmaestro.mohaeng.dto.auth.TokenRefreshRequestDto;
import org.swmaestro.mohaeng.service.auth.AuthService;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final int PREFIX_TOKEN_LENGTH = 7;

    private final AuthService authService;

    /**
     * Access Token Refresh
     * Access Token 만료 시 Refresh Token 통해 refresh
     * @param  request HttpServletRequest
     * @param  tokenRefreshRequestDto TokenRefreshRequestDto
     * @return ResponseEntity with TokenRefreshResponseDto
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> reissueToken(HttpServletRequest request,
                                          @RequestBody TokenRefreshRequestDto tokenRefreshRequestDto) {
        String accessToken = request.getHeader(HEADER_AUTHORIZATION).substring(PREFIX_TOKEN_LENGTH);
        return ResponseEntity.ok().body(authService.reissueToken(accessToken, tokenRefreshRequestDto.getRefreshToken()));
    }
}
