package org.swmaestro.mohaeng.component.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;
import org.swmaestro.mohaeng.service.RedisService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class JwtTokenProviderTest {

    @InjectMocks
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private RedisService redisService;

    private final String defaultSecretKey = "secretTestKeysecretTestKeysecretTestKeysecretTestKey";
    private final long accessTokenValidity = 3600000; // 1 hour in milliseconds for testing
    private final long refreshTokenValidity = 36000000; // 10 hours in milliseconds for testing

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        String jwtSecret = System.getenv("jwt-secret-key");
        if (jwtSecret == null || jwtSecret.isEmpty()) {
            jwtSecret = defaultSecretKey;
        }

        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", jwtSecret);
        ReflectionTestUtils.setField(jwtTokenProvider, "accessTokenValidityInMilliseconds", accessTokenValidity);
        ReflectionTestUtils.setField(jwtTokenProvider, "refreshTokenValidityInMilliseconds", refreshTokenValidity);
    }

    @Test
    @DisplayName("액세스 토큰 생성 테스트")
    public void testCreateAccessToken() {
        String payload = "test@mohaeng.org";
        String token = jwtTokenProvider.createAccessToken(payload);
        assertNotNull(token);
        assertEquals(payload, jwtTokenProvider.getPayload(token));
    }

    @Test
    @DisplayName("리프레시 토큰 생성 테스트")
    public void testCreateRefreshToken() {
        String payload = "test@mohaeng.org";
        String token = jwtTokenProvider.createRefreshToken(payload);
        assertNotNull(token);
    }

    @Test
    @DisplayName("토큰 유효성 검사")
    public void testValidateToken() {
        String payload = "test@mohaeng.org";
        String token = jwtTokenProvider.createAccessToken(payload);
        assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    @DisplayName("잘못된 토큰 테스트")
    public void testInvalidToken() {
        String invalidToken = "invalidToken";
        assertThrows(RuntimeException.class, () -> jwtTokenProvider.getPayload(invalidToken));
        assertFalse(jwtTokenProvider.validateToken(invalidToken));
    }

    @Test
    @DisplayName("만료된 토큰 테스트")
    public void testExpiredToken() throws InterruptedException {
        String payload = "test@mohaeg.org";
        String token = jwtTokenProvider.createToken(payload, -1000);
        assertFalse(jwtTokenProvider.validateToken(token));
    }

    @Test
    @DisplayName("리프레시 토큰 검증 테스트")
    public void testValidateRefreshToken() {
        String userEmail = "test@mohaeng.org";
        String refreshToken = jwtTokenProvider.createRefreshToken(userEmail);
        when(redisService.validateToken(userEmail, refreshToken)).thenReturn(true);

        assertTrue(jwtTokenProvider.validateToken(refreshToken));
    }


    @Test
    @DisplayName("리프레시 토큰 검증 실패 테스트")
    public void testValidateRefreshTokenFail() {
        String invalidRefreshToken = "invalidRefreshToken";
        assertFalse(jwtTokenProvider.validateToken(invalidRefreshToken));
    }


    @Test
    @DisplayName("액세스 토큰 재발급 실패 테스트")
    public void testReissueAccessTokenFail() {
        String invalidRefreshToken = "invalidRefreshToken";
        assertThrows(RuntimeException.class, () -> jwtTokenProvider.reissueAccessToken(invalidRefreshToken));
    }
}