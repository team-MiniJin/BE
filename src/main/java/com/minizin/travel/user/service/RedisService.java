package com.minizin.travel.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate stringRedisTemplate;

    public void saveWithExpirationTime(String key, String value, long duration) {
        stringRedisTemplate.opsForValue().set(key, value, Duration.ofSeconds(duration));
    }

    public String getData(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

}
