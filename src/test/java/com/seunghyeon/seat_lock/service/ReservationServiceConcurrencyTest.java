package com.seunghyeon.seat_lock.service;

import com.seunghyeon.seat_lock.entity.Event;
import com.seunghyeon.seat_lock.entity.Seat;
import com.seunghyeon.seat_lock.entity.SeatType;
import com.seunghyeon.seat_lock.repository.EventRepository;
import com.seunghyeon.seat_lock.repository.SeatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ReservationServiceConcurrencyTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private SeatRepository seatRepository;

    private static final Long SEAT_ID = 1L;


    @Autowired
    private EventRepository eventRepository;


    @BeforeEach
    void cleanUp() {
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

    @Test
    @Transactional
    void  좌석1을_홀드해도_좌석2에_영향이_없다(){
        Long userId = 1L;
        Long userId2 = 2L;
        Event event = Event.builder().name("테스트 콘서트").startedAt(Instant.now()).build();
        eventRepository.save(event);
        Seat seat1 = Seat.builder().event(event).seatNumber("1").price(5000).type(SeatType.GOLD).build();
        seatRepository.save(seat1);
        Seat seat2 = Seat.builder().event(event).seatNumber("2").price(5000).type(SeatType.GOLD).build();
        seatRepository.save(seat2);
        Boolean result1= reservationService.holdSeat(seat1.getId(),userId);
        Boolean result2 =reservationService.holdSeat(seat2.getId(),userId2);
        assertTrue(result1);

        assertTrue(result2);

        assertEquals("1",redisTemplate.opsForValue().get("seat:hold:" + seat1.getId()));

        assertEquals("2",redisTemplate.opsForValue().get("seat:hold:" + seat2.getId()));




    }
}
