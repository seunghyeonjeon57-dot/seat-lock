package com.seunghyeon.seat_lock.repository;

import com.seunghyeon.seat_lock.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event,Long> {
}
