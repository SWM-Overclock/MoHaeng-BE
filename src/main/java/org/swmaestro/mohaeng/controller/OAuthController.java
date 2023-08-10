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
@RequestMapping("/login")
public class OAuthController {
    private final OAuthService oAuthService;

    @PostMapping("/{provider}")
    public ResponseEntity<LoginResponse> login(@PathVariable String provider, @RequestBody LoginRequest loginRequest) {
        log.info("provider: {}, token: {}", provider, loginRequest.getToken());

        LoginResponse loginResponse = oAuthService.login(provider, loginRequest.getToken());
        return ResponseEntity.ok().body(loginResponse);
    }
}
