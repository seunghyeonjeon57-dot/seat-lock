package com.seunghyeon.seat_lock.service;

import com.seunghyeon.seat_lock.entity.Reservation;
import com.seunghyeon.seat_lock.repository.EventRepository;
import com.seunghyeon.seat_lock.repository.ReservationRepository;
import com.seunghyeon.seat_lock.repository.SeatRepository;
import com.seunghyeon.seat_lock.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class ReservationService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final SeatRepository seatRepository;
    private final ReservationRepository reservationRepository;

    public Reservation holdSeat(Long seatId,Long userId){

    }

}
