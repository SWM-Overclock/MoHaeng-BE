package org.swmaestro.mohaeng.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.swmaestro.mohaeng.dto.LoginRequest;
import org.swmaestro.mohaeng.dto.LoginResponse;
import org.swmaestro.mohaeng.service.OAuthService;

@RequiredArgsConstructor
@Slf4j
@RestController
public class OAuthController {
    private final OAuthService oAuthService;

    @PostMapping("/login/oauth/{provider}")
    public ResponseEntity<LoginResponse> login(@PathVariable String provider, @RequestBody LoginRequest loginRequest) {
        log.info("provider: {}, code: {}", provider, loginRequest.getCode());
        LoginResponse loginResponse = oAuthService.login(provider, loginRequest.getCode());
        return ResponseEntity.ok().body(loginResponse);
    }
}
