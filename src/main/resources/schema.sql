DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS schedule_transactions;
DROP TABLE IF EXISTS transactions;

CREATE TABLE users (
    `user_id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '사용자 ID',
    `name`    VARCHAR(32) NOT NULL COMMENT '사용자 이름',
    `grade`   VARCHAR(20) NOT NULL COMMENT '사용자 등급',
    `status`  VARCHAR(30) NOT NULL COMMENT '사용자 상태'
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '유저 정보';

CREATE TABLE accounts (
    `account_id`     BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '계좌 ID',
    `account_number` VARCHAR(20)    NOT NULL COMMENT '계좌 번호',
    `user_id`        BIGINT         NOT NULL COMMENT '사용자 ID',
    `balance`        DECIMAL(18, 2) NOT NULL COMMENT '계좌 잔액',
    `type`           VARCHAR(64)    NOT NULL COMMENT '계좌 유형'
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '계좌 정보';

CREATE TABLE schedule_transactions (
    schedule_transaction_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '예약 거래 ID',
    source_account_id       BIGINT         NOT NULL COMMENT '출금 계좌 ID',
    destination_account_id  BIGINT         NOT NULL COMMENT '입금 계좌 ID',
    amount                  DECIMAL(18, 2) NULL COMMENT '거래 금액',
    status                  VARCHAR(30)    NOT NULL COMMENT '거래 상태',
    failure_reason          VARCHAR(128)   NULL COMMENT '실패 사유',
    schedule_dt             DATE           NOT NULL COMMENT '예약 날짜',
    reserved_at             DATETIME       NOT NULL COMMENT '예약 생성 일시',
    executed_at             DATETIME       NULL COMMENT '실행 일시',
    memo                    VARCHAR(255)   NULL COMMENT '메모'
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '예약 거래 정보';

CREATE TABLE transactions (
    transaction_id          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '거래 ID',
    transaction_uuid        BINARY(16)     NOT NULL COMMENT '거래 UUID',
    account_id              BIGINT         NOT NULL COMMENT '본인 계좌 ID',
    counterparty_account_id BIGINT         NOT NULL COMMENT '상대 계좌 ID',
    amount                  DECIMAL(18, 2) NOT NULL COMMENT '거래 금액',
    balance                 DECIMAL(18, 2) NOT NULL COMMENT '거래 후 잔액',
    type                    VARCHAR(20)    NOT NULL COMMENT '거래 유형',
    status                  VARCHAR(30)    NOT NULL COMMENT '거래 상태',
    failure_reason          VARCHAR(128)   NULL COMMENT '실패 사유',
    schedule_transaction_id BIGINT         NULL COMMENT '예약 거래 ID',
    requested_at            DATETIME       NOT NULL COMMENT '요청 일시',
    transferred_at          DATETIME       NULL COMMENT '전송 일시',
    memo                    VARCHAR(255)   NULL COMMENT '메모'
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '거래 정보';
