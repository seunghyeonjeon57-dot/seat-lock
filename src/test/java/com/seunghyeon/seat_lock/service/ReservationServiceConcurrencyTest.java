package com.seunghyeon.seat_lock.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ReservationServiceConcurrencyTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final Long SEAT_ID = 1L;

    @BeforeEach
    void cleanUp() {
        // 이전 테스트 실행에서 남은 홀드가 있으면 지워서, 매번 같은 조건에서 시작하게 함
        redisTemplate.delete("seat:hold:" + SEAT_ID);
    }

    @Test
    void 동시에_같은_좌석을_홀드하면_한_명만_성공한다() throws InterruptedException {
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            long userId = i + 1;
            executorService.submit(() -> {
                try {
                    startLatch.await();
                    boolean result = reservationService.holdSeat(SEAT_ID, userId);
                    if (result) {
                        successCount.incrementAndGet();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        doneLatch.await();
        executorService.shutdown();

        assertEquals(1, successCount.get());
    }
}
