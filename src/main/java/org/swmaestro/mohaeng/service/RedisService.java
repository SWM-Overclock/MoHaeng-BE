package org.swmaestro.mohaeng.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate redisTemplate;

    public void setDataWithExpiration(String key, String value, long expireTime) {
        redisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.MILLISECONDS);
    }

    public String getData(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    public boolean validateToken(String userEmail, String refreshToken) {
        String storedToken = getData(userEmail);
        return refreshToken.equals(storedToken);
    }
}