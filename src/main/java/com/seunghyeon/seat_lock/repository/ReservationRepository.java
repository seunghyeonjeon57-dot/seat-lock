package com.seunghyeon.seat_lock.repository;

import com.seunghyeon.seat_lock.entity.Reservation;
import com.seunghyeon.seat_lock.entity.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {

    public boolean existsBySeatIdAndStatus(Long seatId, ReservationStatus status);
}
