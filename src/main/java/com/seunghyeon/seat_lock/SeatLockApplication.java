package com.seunghyeon.seat_lock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SeatLockApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeatLockApplication.class, args);
	}

}
