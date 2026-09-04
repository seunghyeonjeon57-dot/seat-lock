package com.seunghyeon.seat_lock.exception;


public class SeatNotFoundException extends RuntimeException {
    public SeatNotFoundException(String message) {
        super(message);
    }
}
