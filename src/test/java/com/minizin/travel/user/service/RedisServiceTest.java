package com.minizin.travel.user.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RedisServiceTest {

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @InjectMocks
    private RedisService redisService;

    @Test
    @DisplayName("인증 코드 만료 시간과 함께 저장")
    void saveWithExpirationTime() {
        //given
        String key = "key";
        String value = "value";
        long duration = 10L;

        ValueOperations<String, String> valueOperations = mock(ValueOperations.class);
        given(stringRedisTemplate.opsForValue()).willReturn(valueOperations);

        //when
        redisService.saveWithExpirationTime(key, value, duration);

        //then
        verify(valueOperations, times(1)).set(eq(key), eq(value),
                eq(Duration.ofSeconds(duration)));
    }

    @Test
    @DisplayName("데이터 가져오기")
    void getData() {
        //given
        String key = "key";
        String value = "value";

        ValueOperations<String, String> valueOperations = mock(ValueOperations.class);
        given(stringRedisTemplate.opsForValue()).willReturn(valueOperations);
        when(valueOperations.get(key)).thenReturn(value);

        //when
        String data = redisService.getData(key);

        //then
        assertEquals(value, data);
    }

}