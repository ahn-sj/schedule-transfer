## 도메인 모델

### 유저 (User)

#### 속성
- `name`: 유저 이름
- `grade`: 유저 등급
- `status`: 상태

#### 행위
- `create()`: 유저 생성
- `deactivate()`: 탈퇴 처리
- `activate()`: 활성화 처리
- `dormantize()`: 휴면 처리
- `reactivate()`: 휴면 해제

#### 규칙
- 탈퇴는 활성화 또는 휴면 상태일때에만 가능합니다.
- 휴면 해제는 휴면 처리된 상태일때에만 가능합니다.

---

### 유저 등급 (UserGrade)

#### 상수

- `BASIC`
- `SILVER`
- `GOLD`
- `PLATINUM`
- `DIAMOND`
- `VIP`
- `VVIP`

---

### 유저 상태 (UserStatus)

#### 상수
- `ACTIVE`: 활성화 상태
- `DORMANT`: 휴면 상태
- `DEACTIVATED`: 탈퇴 상태

---

### 유저 계좌 (User Account)

#### 속성
- `user_id`: 가입 유저의 계좌
- `account_type`: 계좌 유형 (보통예금, 적금 등)
- `balance`: 계좌 잔액

#### 행위
- `canWithdraw(amount)`: 잔액 체크

#### 규칙
- 계좌 거래는 유저가 활성화 상태여야만 가능합니다.
- 계좌 유형에 따라 계좌 개설 가능 개수가 달라집니다.
    - 여기에서는 보통 계좌만을 개설한다고 가정

---

### 계좌 유형 (AccountType)

#### 상수

- `SAVINGS`: 보통 예금 계좌
- `INSTALLMENT_SAVINGS`: 적금 계좌

---

### 예약 거래 (Schedule Transaction)

#### 속성
- `source_account`: 송금 계좌
- `destination_account`: 수취인 계좌
- `amount`: 송금액
- `status`: 예약 거래 상태
- `memo`: 예약 거래 메모
- `failure_reason`: 실패 사유 (실패시)
- `reserved_at`: 예약 거래 신청 일시
- `scheduled_dt`: 예약 거래 일자 (실제 이 날짜에 송금액만큼 수신자에게 발송)
- `executed_at`: 실제 실행 일시

#### 행위
- `scheduleTransaction()`: 예약 거래를 등록

#### 규칙
- 예약 송금은 설정된 날짜 당일 오전 9시를 시작으로 순차적으로 송금이 진행됩니다. (상황에 따라 오전 11시 이후에 출금될 수 있음)
- 시간대를 설정할 수 없으며 일자별로 송금을 예약할 수 있습니다
- 1회 200만 원까지 자동이체할 수 있음
- 은행 점검 시간에는 처리가 불가능함
- 잔액 부족 시 재처리 없음
- 당일 등록 허용이 가능하지만(6월 23일 23시 59분에 6월 23일로 예약 등록 케이스 포함), 당일 배치 시간(오전 9시)이 지났으므로 실제로는 6월 24일 9시부터 순차적으로 실행됨

---

### 거래 (Transaction)

#### 속성

- `transaction_uuid`: 거래 고유 ID (UUID v7)
- `account_id`: 송금 계좌
- `counterparty_account`: 상대 계좌
- `transaction_type`: 거래 유형 (출금, 입금)
- `amount`: 거래 금액
- `balance`: 거래 이후 잔액
- `transaction_status`: 거래 상태
- `scheduled_transaction_id`: 예약 거래
- `failure_reason`: 실패 사유
- `memo`: 거래 메모
- `transaction_at`: 거래 일시

> (optional) fee_amount: 추후 수수료 정책도 포함되면 추가해보기

#### 행위
- `Transaction debit(Account account, Account counterparty, amount)`: 출금
- `Transaction credit(Account account, Account counterparty, amount)`: 입금
- `fail()`: 실패 처리

#### 규칙
- lock/atomicity 설계 필요함

---

### 거래 유형 (TransactionType)
- `DEBIT`: 출금
- `CREDIT`: 입금

### 거래 상태 (TransactionStatus)

#### 상수

- PENDING: 처리 대기
- PROCESSING: 처리중
- SUCCESS: 송금 성공
- FAILED: 송금 실패
- USER_CANCELLED: 사용자 취소

---

### 예약 거래 한도 정책 (Schedule Limit Policy)

#### 행위

- `getGrade(Grade)`: 현재 등급으로 정책 조회
- `checkLimitCompliance(Int)`: 현재 예약된 건수 한도 준수 여부 확인