package com.seunghyeon.seat_lock.store;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

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

    public boolean releaseHold(Long seatId,Long userId){
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setScriptText(
                "if redis.call('GET', KEYS[1]) == ARGV[1] then "  +
                "return redis.call('DEL' , KEYS[1])" +
                "else return 0 end");
        script.setResultType(Long.class);
        String key = "seat:hold:" + seatId;
        String value = userId.toString();
        Long result = template.execute(script, List.of(key),value);
        return result != null && result  == 1L;
    }

    public Optional<Long> getHoldOwner(Long seatId){
        String key = "seat:hold:" + seatId;
        String value = template.opsForValue().get(key);
        return Optional.ofNullable(value).map(Long::valueOf);

    }




}
