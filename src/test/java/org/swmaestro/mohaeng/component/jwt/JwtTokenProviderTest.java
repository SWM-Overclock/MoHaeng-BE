package org.swmaestro.mohaeng.component.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

public class JwtTokenProviderTest {

    @InjectMocks
    private JwtTokenProvider jwtTokenProvider;

    private final String defaultSecretKey = "secret-test-key";
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
        String payload = "test@mohaneg.org";
        String token = jwtTokenProvider.createAccessToken(payload);
        assertNotNull(token);
        assertEquals(payload, jwtTokenProvider.getPayload(token));
    }

    @Test
    @DisplayName("리프레시 토큰 생성 테스트")
    public void testCreateRefreshToken() {
        String token = jwtTokenProvider.createRefreshToken();
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
}