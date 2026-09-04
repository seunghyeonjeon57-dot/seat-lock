package com.seunghyeon.seat_lock;

import com.seunghyeon.seat_lock.dto.HoldRequest;
import com.seunghyeon.seat_lock.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservaiton")
public class ReservationController {
    private final ReservationService service;

    @GetMapping("/hold/{seatId}")
    public ResponseEntity<?> holdSeat(
            @PathVariable Long seatId,
            @RequestBody HoldRequest request
            ) {
        boolean result =service.holdSeat(seatId,request.userId());
        if(result){
            return ResponseEntity.ok(Map.of("message","홀드 성공"));
        }else{
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message","다른 사용자가 홀드 중입니다."));
        }


    }
    @PostMapping("/api/seats/{seatId}/release")
    public ResponseEntity<?> releaseHold(@PathVariable Long seatId, @RequestBody HoldRequest request) {
        boolean success = service.holdReleaseSeat(seatId, request.userId());

        if (success) {
            return ResponseEntity.ok(Map.of("message", "홀드 해제 성공"));
        } else {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "해제할 수 없습니다 (본인 홀드가 아니거나 이미 해제됨)"));
        }
    }







}
