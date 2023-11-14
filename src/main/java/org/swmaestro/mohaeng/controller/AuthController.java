package org.swmaestro.mohaeng.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.swmaestro.mohaeng.component.jwt.JwtTokenProvider;
import org.swmaestro.mohaeng.dto.TokenRequest;
import org.swmaestro.mohaeng.dto.TokenResponse;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/token-reissue")
    public ResponseEntity<?> reissueToken(@RequestBody TokenRequest tokenRequest) {
        log.info("refreshToken: {}", tokenRequest.getRefreshToken());
        String newAccessToken = jwtTokenProvider.reissueAccessToken(tokenRequest.getRefreshToken());
        return ResponseEntity.ok(new TokenResponse(newAccessToken));
    }
}
