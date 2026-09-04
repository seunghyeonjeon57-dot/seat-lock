package com.seunghyeon.seat_lock.exception;

import org.springframework.http.HttpStatus;

public class SeatNotFoundException extends RuntimeException {
    public SeatNotFoundException(String message) {
        super(message);
    }
}
