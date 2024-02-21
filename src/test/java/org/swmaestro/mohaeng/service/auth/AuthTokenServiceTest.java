package org.swmaestro.mohaeng.service.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.swmaestro.mohaeng.service.auth.AuthTokenService;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AuthTokenServiceTest {


    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private AuthTokenService authTokenService;

    @BeforeEach
    public void setup() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void 토큰과_만료시간을_함께_레디스에_저장한다() {
        // given
        String key = "test@mohaeng.org";
        String value = "tokenRefreshToken";
        long expireTime = 10000; // 10초

        // when
        authTokenService.setDataWithExpiration(key, value, expireTime);

        // then
        verify(valueOperations).set(eq(key), eq(value), eq(expireTime), eq(TimeUnit.MILLISECONDS));
    }

    @Test
    void 주어진_키로_레디스에서_데이터를_가져온다() {
        // given
        String key = "test@mohaeng.org";
        String expectedValue = "tokenValue";
        when(valueOperations.get(key)).thenReturn(expectedValue);

        // when
        String actualValue = authTokenService.getData(key);

        // then
        verify(valueOperations).get(key);
        assertThat(actualValue).isEqualTo(expectedValue);
    }
}