package com.seunghyeon.seat_lock.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Table(name = "events")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private Instant startedAt;
}
