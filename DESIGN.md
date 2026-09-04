# seat-lock 설계 문서

## 전체 로드맵

1. **동시성 제어(좌석 홀드) 최소 구현 + 테스트** — 현재 진행 중
2. 위 단계 검증 끝나면, 기능을 확장해서 실제 콘서트 사이트처럼 만들기
   - Kafka로 예약 관련 알림 이벤트 전달
   - 콘서트 외부 API(KOPIS) 연동으로 실제 이벤트 데이터 사용

지금 단계의 목적은 "동시성 제어 자체"를 증명하는 것. 예약 상태 관리(홀드→확정 전환, 결제 등)는 다음 확장 단계의 영역으로 미룸.

## 좌석 홀드 설계

### RedisHoldStore.holdSeat(seatId, userId) — 완료

- Redis SETNX(`opsForValue().setIfAbsent`) 기반 분산 락
- key: `seat:hold:{seatId}`, value: userId, TTL 5분
- 반환: `boolean` — 성공(홀드 획득) / 실패(이미 누가 홀드 중)
- `Boolean.TRUE.equals(result)`로 null-safe 처리 (트랜잭션/파이프라인 모드에서 결과가 null일 수 있어 unboxing NPE 방지)

### ReservationService.holdSeat(seatId, userId) — 설계 중

**결정: 방향 B 채택** — 이 단계에서는 Redis 홀드 성공/실패만으로 동시성 제어를 검증하고, DB에 Reservation row는 만들지 않는다.

- 이유: `ReservationStatus`가 현재 `CONFIRMED`/`CANCELLED`뿐이라 "홀드 중" 상태가 없음. 지금 이 상태 모델까지 확장하는 건 동시성 제어 검증과 무관한 "기능 확장" 영역이라 우선순위 밀림.
- 따라서 리턴 타입을 `Reservation`에서 다른 형태로 바꿔야 함 — `boolean` 단순 성공/실패로 갈지, 실패 사유(이미 홀드됨 / 존재하지 않는 좌석 등)를 구분할 결과 객체로 갈지는 아직 미결정.

### 아직 안 건드린 부분 (참고용, 나중에 확장 단계에서 다룸)

- `ReservationStatus`에 `HOLD`/`PENDING` 같은 중간 상태 없음
- `Seat` 엔티티에 예약 가능 여부를 나타내는 상태 필드 없음 — 지금은 Redis만으로 동시성 제어, "이미 결제 확정된 좌석"인지 체크하는 로직은 미구현
