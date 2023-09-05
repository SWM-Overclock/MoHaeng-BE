package org.swmaestro.mohaeng.service;

import com.nimbusds.openid.connect.sdk.federation.policy.operations.ValueOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class RedisServiceTest {

    @InjectMocks
    private RedisService redisService;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @BeforeEach
    public void setup() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    public void testSetDataWithExpiration() {
        String key = "testKey";
        String value = "testValue";
        long expireTime = 1000L;

        redisService.setDataWithExpiration(key, value, expireTime);

        verify(valueOperations, times(1)).set(key, value, expireTime, TimeUnit.MILLISECONDS);
    }

    @Test
    public void getDataTest() {
        String key = "key";
        String value = "value";

        when(valueOperations.get(key)).thenReturn(value);

        String result = redisService.getData(key);

        assertEquals(value, result);
    }

    @Test
    public void validateTokenSuccessTest() {
        String userEmail = "test@mohaeng.org";
        String refreshToken = "refreshToken";

        when(valueOperations.get(userEmail)).thenReturn(refreshToken);

        assertTrue(redisService.validateToken(userEmail, refreshToken));
    }

    @Test
    public void validateTokenFailTest() {
        String userEmail = "test@mohaeng.org";
        String storedToken = "storedToken";
        String wrongToken = "wrongToken";

        when(valueOperations.get(userEmail)).thenReturn(storedToken);

        assertFalse(redisService.validateToken(userEmail, wrongToken));
    }
}