# Goal

- 하나의 사용자(user)가 동일한 날짜(scheduleDate)에 예약 이체를 최대 10건까지만 등록할 수 있어야 함
- 다중 스레드 환경에서 동일 사용자의 이체 등록 시 동시성 보장이 필요

## Solution

| 도구 | 설명 |
|------|------|
| ReetrantLock + Map | 사용자별 락 객체를 Map에 저장하고 동기화 |
| Redis + Lua Script (Atomic) | Redis에서 하루 예약 건수 key 관리하고 Lua 스크립트로 원자적 증가 |
| Redisson Distributed Lock | 분산 환경에서 Lock을 이용한 블로킹 처리 |
