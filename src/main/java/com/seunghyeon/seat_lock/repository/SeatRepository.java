package com.seunghyeon.seat_lock.repository;

import com.seunghyeon.seat_lock.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat,Long> {
}
