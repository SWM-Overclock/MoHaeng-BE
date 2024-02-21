package org.swmaestro.mohaeng.component.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.swmaestro.mohaeng.util.exception.InvalidRefreshTokenException;
import org.swmaestro.mohaeng.util.exception.NotExpiredTokenException;
import org.swmaestro.mohaeng.util.exception.RefreshTokenMismatchException;
import org.swmaestro.mohaeng.service.RedisService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JwtTokenProviderTest {

    private static final long accessTokenValidity = 1000 * 60 * 60;
    private static final long refreshTokenValidity = 1000 * 60 * 60 * 24 * 7;
    private static final String secretKey = "X3qXTkZpNjJnKzZNVFd3eHpFUnRIZWdrbGtjZ0ZuZ2pQUXpRbGVtQUFBQUJ3PT0=";

    @InjectMocks
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private RedisService redisService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(jwtTokenProvider, "accessTokenValidityInMilliseconds", accessTokenValidity);
        ReflectionTestUtils.setField(jwtTokenProvider, "refreshTokenValidityInMilliseconds", refreshTokenValidity);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", secretKey);
    }

    @Test
    public void 액세스토큰_생성이_올바르게_되는지_확인() {
        // given
        String userEmail = "test@mohaeng.org";

        // when
        String token = jwtTokenProvider.createAccessToken(userEmail);

        // then
        assertThat(token).isNotNull();
        assertThat(jwtTokenProvider.getPayload(token)).isEqualTo(userEmail);
    }

    @Test
    public void 리프레쉬토큰_생성_및_Redis_저장_확인() {
        // given
        String userEmail = "test@mohaeng.org";

        // when
        String refreshToken = jwtTokenProvider.createRefreshToken(userEmail);

        // then
        assertThat(refreshToken).isNotNull();
        verify(redisService).setDataWithExpiration(userEmail, refreshToken, refreshTokenValidity);
    }

    @Test
    public void 유효한_토큰의_검증_확인() {
        // given
        String userEmail = "test@mohaeng.org";
        String validToken = jwtTokenProvider.createAccessToken(userEmail);

        // when
        boolean isValid = jwtTokenProvider.validateToken(validToken);

        // then
        assertThat(isValid).isTrue();
        assertThat(jwtTokenProvider.getPayload(validToken)).isEqualTo(userEmail);
    }

    @Test
    public void 만료되지_않은_토큰_재발급_시_예외_발생_확인() {
        // given
        String userEmail = "test@mohaeng.org";
        String accessToken = jwtTokenProvider.createAccessToken(userEmail);
        String refreshToken = jwtTokenProvider.createRefreshToken(userEmail);

        // when & then
        assertThrows(NotExpiredTokenException.class, () -> {
            jwtTokenProvider.reissueAccessToken(accessToken, refreshToken);
        });
    }

    @Test
    public void 만료된_토큰으로_새_액세스토큰_재발급_확인() {
        // given
        ReflectionTestUtils.setField(jwtTokenProvider, "accessTokenValidityInMilliseconds", -accessTokenValidity);
        String userEmail = "test@example.com";
        String expiredAccessToken = jwtTokenProvider.createAccessToken(userEmail);
        String validRefreshToken = jwtTokenProvider.createRefreshToken(userEmail);

        when(redisService.getData(anyString())).thenReturn(validRefreshToken);

        ReflectionTestUtils.setField(jwtTokenProvider, "accessTokenValidityInMilliseconds", accessTokenValidity);

        // when
        String newAccessToken = jwtTokenProvider.reissueAccessToken(expiredAccessToken, validRefreshToken);

        // then
        assertNotNull(newAccessToken);
        assertThat(jwtTokenProvider.getPayload(newAccessToken)).isEqualTo(userEmail);
    }

    @Test
    public void 리프레쉬_토큰_불일치_시_예외_발생_확인() {
        // given
        ReflectionTestUtils.setField(jwtTokenProvider, "accessTokenValidityInMilliseconds", -accessTokenValidity);
        String userEmail = "test@example.com";
        String invalidEmail = "invalid@example.com";
        String expiredAccessToken = jwtTokenProvider.createAccessToken(userEmail);
        String validRefreshToken = jwtTokenProvider.createRefreshToken(userEmail);
        String invalidRefreshToken = jwtTokenProvider.createRefreshToken(invalidEmail);

        when(redisService.getData(anyString())).thenReturn(validRefreshToken);

        ReflectionTestUtils.setField(jwtTokenProvider, "accessTokenValidityInMilliseconds", accessTokenValidity);

        // when & then
        assertThrows(RefreshTokenMismatchException.class, () -> {
            jwtTokenProvider.reissueAccessToken(expiredAccessToken, invalidRefreshToken);
        });

    }

    @Test
    public void 리프레쉬_토큰_유효하지_않을_때_예외_발생() {
        // given
        ReflectionTestUtils.setField(jwtTokenProvider, "accessTokenValidityInMilliseconds", -accessTokenValidity);
        String userEmail = "test@example.com";
        String expiredAccessToken = jwtTokenProvider.createAccessToken(userEmail);
        String validRefreshToken = jwtTokenProvider.createRefreshToken(userEmail);
        String invalidRefreshToken = "invalid_refresh_token";

        when(redisService.getData(anyString())).thenReturn(validRefreshToken);

        ReflectionTestUtils.setField(jwtTokenProvider, "accessTokenValidityInMilliseconds", accessTokenValidity);

        // when & then
        assertThrows(InvalidRefreshTokenException.class, () -> {
            jwtTokenProvider.reissueAccessToken(expiredAccessToken, invalidRefreshToken);
        });
    }
}