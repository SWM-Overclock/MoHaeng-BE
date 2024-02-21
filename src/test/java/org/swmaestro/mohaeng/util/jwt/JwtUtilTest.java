package org.swmaestro.mohaeng.util.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.swmaestro.mohaeng.util.exception.InvalidRefreshTokenException;
import org.swmaestro.mohaeng.util.exception.NotExpiredTokenException;
import org.swmaestro.mohaeng.util.exception.RefreshTokenMismatchException;
import org.swmaestro.mohaeng.service.auth.AuthTokenService;
import org.swmaestro.mohaeng.util.jwt.JwtUtil;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JwtUtilTest {

    private static final long accessTokenValidity = 1000 * 60 * 60;
    private static final long refreshTokenValidity = 1000 * 60 * 60 * 24 * 7;
    private static final String secretKey = "X3qXTkZpNjJnKzZNVFd3eHpFUnRIZWdrbGtjZ0ZuZ2pQUXpRbGVtQUFBQUJ3PT0=";

    @InjectMocks
    private JwtUtil jwtUtil;

    @Mock
    private AuthTokenService redisService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(jwtUtil, "accessTokenValidityInMilliseconds", accessTokenValidity);
        ReflectionTestUtils.setField(jwtUtil, "refreshTokenValidityInMilliseconds", refreshTokenValidity);
        ReflectionTestUtils.setField(jwtUtil, "secretKey", secretKey);
    }

    @Test
    public void 액세스토큰_생성이_올바르게_되는지_확인() {
        // given
        String userEmail = "test@mohaeng.org";

        // when
        String token = jwtUtil.createAccessToken(userEmail);

        // then
        assertThat(token).isNotNull();
        assertThat(jwtUtil.getPayload(token)).isEqualTo(userEmail);
    }

    @Test
    public void 리프레쉬토큰_생성_및_Redis_저장_확인() {
        // given
        String userEmail = "test@mohaeng.org";

        // when
        String refreshToken = jwtUtil.createRefreshToken(userEmail);

        // then
        assertThat(refreshToken).isNotNull();
        verify(redisService).setDataWithExpiration(userEmail, refreshToken, refreshTokenValidity);
    }

    @Test
    public void 유효한_토큰의_검증_확인() {
        // given
        String userEmail = "test@mohaeng.org";
        String validToken = jwtUtil.createAccessToken(userEmail);

        // when
        boolean isValid = jwtUtil.validateToken(validToken);

        // then
        assertThat(isValid).isTrue();
        assertThat(jwtUtil.getPayload(validToken)).isEqualTo(userEmail);
    }

    @Test
    public void 만료되지_않은_토큰_재발급_시_예외_발생_확인() {
        // given
        String userEmail = "test@mohaeng.org";
        String accessToken = jwtUtil.createAccessToken(userEmail);
        String refreshToken = jwtUtil.createRefreshToken(userEmail);

        // when & then
        assertThrows(NotExpiredTokenException.class, () -> {
            jwtUtil.reissueAccessToken(accessToken, refreshToken);
        });
    }

    @Test
    public void 만료된_토큰으로_새_액세스토큰_재발급_확인() {
        // given
        ReflectionTestUtils.setField(jwtUtil, "accessTokenValidityInMilliseconds", -accessTokenValidity);
        String userEmail = "test@example.com";
        String expiredAccessToken = jwtUtil.createAccessToken(userEmail);
        String validRefreshToken = jwtUtil.createRefreshToken(userEmail);

        when(redisService.getData(anyString())).thenReturn(validRefreshToken);

        ReflectionTestUtils.setField(jwtUtil, "accessTokenValidityInMilliseconds", accessTokenValidity);

        // when
        String newAccessToken = jwtUtil.reissueAccessToken(expiredAccessToken, validRefreshToken);

        // then
        assertNotNull(newAccessToken);
        assertThat(jwtUtil.getPayload(newAccessToken)).isEqualTo(userEmail);
    }

    @Test
    public void 리프레쉬_토큰_불일치_시_예외_발생_확인() {
        // given
        ReflectionTestUtils.setField(jwtUtil, "accessTokenValidityInMilliseconds", -accessTokenValidity);
        String userEmail = "test@example.com";
        String invalidEmail = "invalid@example.com";
        String expiredAccessToken = jwtUtil.createAccessToken(userEmail);
        String validRefreshToken = jwtUtil.createRefreshToken(userEmail);
        String invalidRefreshToken = jwtUtil.createRefreshToken(invalidEmail);

        when(redisService.getData(anyString())).thenReturn(validRefreshToken);

        ReflectionTestUtils.setField(jwtUtil, "accessTokenValidityInMilliseconds", accessTokenValidity);

        // when & then
        assertThrows(RefreshTokenMismatchException.class, () -> {
            jwtUtil.reissueAccessToken(expiredAccessToken, invalidRefreshToken);
        });

    }

    @Test
    public void 리프레쉬_토큰_유효하지_않을_때_예외_발생() {
        // given
        ReflectionTestUtils.setField(jwtUtil, "accessTokenValidityInMilliseconds", -accessTokenValidity);
        String userEmail = "test@example.com";
        String expiredAccessToken = jwtUtil.createAccessToken(userEmail);
        String validRefreshToken = jwtUtil.createRefreshToken(userEmail);
        String invalidRefreshToken = "invalid_refresh_token";

        when(redisService.getData(anyString())).thenReturn(validRefreshToken);

        ReflectionTestUtils.setField(jwtUtil, "accessTokenValidityInMilliseconds", accessTokenValidity);

        // when & then
        assertThrows(InvalidRefreshTokenException.class, () -> {
            jwtUtil.reissueAccessToken(expiredAccessToken, invalidRefreshToken);
        });
    }
}