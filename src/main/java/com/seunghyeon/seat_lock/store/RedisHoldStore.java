package com.seunghyeon.seat_lock.store;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class RedisHoldStore {
    private final StringRedisTemplate template;

    public boolean holdSeat(Long seatId,Long userId){
        String key = "seat:hold:" + seatId;
        String value = userId.toString();
        Boolean result = template.opsForValue().setIfAbsent(key,value, Duration.ofMinutes(5));
        return Boolean.TRUE.equals(result);
    }





}
