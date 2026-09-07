package com.seunghyeon.seat_lock.service;

import com.seunghyeon.seat_lock.exception.SeatNotFoundException;
import com.seunghyeon.seat_lock.repository.EventRepository;
import com.seunghyeon.seat_lock.repository.ReservationRepository;
import com.seunghyeon.seat_lock.repository.SeatRepository;
import com.seunghyeon.seat_lock.repository.UserRepository;
import com.seunghyeon.seat_lock.store.RedisHoldStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class ReservationService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final SeatRepository seatRepository;
    private final ReservationRepository reservationRepository;
    private final RedisHoldStore store;

    public Boolean holdSeat(Long seatId,Long userId){
     seatRepository.findById(seatId)
                .orElseThrow(()->new SeatNotFoundException("좌석을 찾을 수 없습니다."));
        return store.holdSeat(seatId,userId);
    }

    public Boolean holdReleaseSeat(Long seatId,Long userId){
        seatRepository.findById(seatId)
                .orElseThrow(()->new SeatNotFoundException("좌석을 찾을 수 없습니다."));
        return store.releaseHold(seatId,userId);
    }

}
