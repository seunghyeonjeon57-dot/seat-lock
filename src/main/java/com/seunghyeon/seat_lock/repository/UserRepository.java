package com.seunghyeon.seat_lock.repository;

import com.seunghyeon.seat_lock.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
