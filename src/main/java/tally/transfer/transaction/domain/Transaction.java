package tally.transfer.transaction.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.Assert;
import tally.transfer.account.domain.Account;
import tally.transfer.account.domain.vo.Money;
import tally.transfer.common.provider.IdentifierGenerator;
import tally.transfer.transaction.domain.enums.TransactionFailureReason;
import tally.transfer.transaction.domain.enums.TransactionStatus;
import tally.transfer.transaction.domain.enums.TransactionType;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(name = "transactions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long id;

    @Column(name = "transaction_uuid", nullable = false, unique = true)
    private UUID uuid;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private Account account; /* 송금자 계좌 */

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private Account counterpartyAccount; /* 수신자 계좌 */

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @Embedded
    private Money amount; /* 거래 금액 */

    @Embedded
    private Money balance; /* 거래 후 잔액 */

    @Column(nullable = true)
    private Long scheduleTransactionId; /* 예약 거래 ID, 예약 거래가 아닌 경우 null */

    @Enumerated(EnumType.STRING)
    private TransactionFailureReason failureReason;

    private String memo;

    @CreatedDate
    private ZonedDateTime requestedAt;

    private ZonedDateTime transferredAt; /* 송금 완료 시간, 송금이 완료되지 않은 경우 null */

    public Transaction(
            final UUID uuid,
            final Account account,
            final Account counterpartyAccount,
            final TransactionType type,
            final TransactionStatus status,
            final Money amount,
            final Money balance,
            final Long scheduleTransactionId,
            final TransactionFailureReason failureReason,
            final String memo
    ) {
        Assert.notNull(uuid, "거래 UUID는 null일 수 없습니다.");
        Assert.notNull(account, "송금자 계좌는 null일 수 없습니다.");
        Assert.notNull(counterpartyAccount, "수신자 계좌는 null일 수 없습니다.");
        Assert.notNull(type, "거래 유형은 null일 수 없습니다.");
        Assert.notNull(status, "거래 상태는 null일 수 없습니다.");
        Assert.notNull(amount, "거래 금액은 null일 수 없습니다.");
        Assert.notNull(balance, "거래 후 잔액은 null일 수 없습니다.");

        this.account = account;
        this.counterpartyAccount = counterpartyAccount;
        this.type = type;
        this.status = status;
        this.amount = amount;
        this.balance = balance;
        this.scheduleTransactionId = scheduleTransactionId;
        this.failureReason = failureReason;
        this.memo = memo;
    }

    public static Transaction debit(
            final IdentifierGenerator generator,
            final Account account,
            final Account counterparty,
            final TransactionStatus status,
            final Money amount,
            final Money balance,
            final Long scheduleTransactionId,
            final TransactionFailureReason failureReason
    ) {
        return new Transaction(generator.generate(), account, counterparty, TransactionType.DEBIT, status, amount, balance, null, failureReason, null);
    }

    public static Transaction credit(
            final IdentifierGenerator generator,
            final Account account,
            final Account counterparty,
            final TransactionStatus status,
            final Money amount,
            final Money balance,
            final Long scheduleTransactionId,
            final TransactionFailureReason failureReason
    ) {
        return new Transaction(generator.generate(), account, counterparty, TransactionType.CREDIT, status, amount, balance, null, failureReason, null);
    }

}
