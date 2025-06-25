package tally.transfer.transaction.domain;

import tally.transfer.account.domain.enums.BankCode;
import tally.transfer.account.domain.vo.Money;
import tally.transfer.transaction.domain.enums.ScheduleTransactionStatus;
import tally.transfer.transaction.domain.enums.TransactionFailureReason;

import java.time.LocalDate;
import java.time.ZonedDateTime;

public class ScheduleTransactionFixture {

    private Long id = null;
    private BankCode sourceBank = null;
    private Long sourceAccountId = null;
    private BankCode targetBank = null;
    private Long destinationAccountId = null;
    private Money amount = Money.ZERO;
    private ScheduleTransactionStatus status = ScheduleTransactionStatus.RESERVED;
    private TransactionFailureReason failureReason = null;
    private String memo = null;
    private LocalDate scheduleDt = LocalDate.now();
    private ZonedDateTime reservedAt = ZonedDateTime.now();
    private ZonedDateTime executedAt = null;

    public static ScheduleTransactionFixture aScheduleTransaction() {
        return new ScheduleTransactionFixture();
    }

    public ScheduleTransactionFixture withId(Long id) {
        this.id = id;
        return this;
    }

    public ScheduleTransactionFixture withSourceBank(BankCode sourceBank) {
        this.sourceBank = sourceBank;
        return this;
    }

    public ScheduleTransactionFixture withSourceAccountId(Long sourceAccountId) {
        this.sourceAccountId = sourceAccountId;
        return this;
    }

    public ScheduleTransactionFixture withTargetBank(BankCode targetBank) {
        this.targetBank = targetBank;
        return this;
    }

    public ScheduleTransactionFixture withDestinationAccountId(Long destinationAccountId) {
        this.destinationAccountId = destinationAccountId;
        return this;
    }

    public ScheduleTransactionFixture withAmount(Money amount) {
        this.amount = amount;
        return this;
    }

    public ScheduleTransactionFixture withStatus(ScheduleTransactionStatus status) {
        this.status = status;
        return this;
    }

    public ScheduleTransactionFixture withFailureReason(TransactionFailureReason failureReason) {
        this.failureReason = failureReason;
        return this;
    }

    public ScheduleTransactionFixture withMemo(String memo) {
        this.memo = memo;
        return this;
    }

    public ScheduleTransactionFixture withScheduleDt(LocalDate scheduleDt) {
        this.scheduleDt = scheduleDt;
        return this;
    }

    public ScheduleTransactionFixture withReservedAt(ZonedDateTime reservedAt) {
        this.reservedAt = reservedAt;
        return this;
    }

    public ScheduleTransactionFixture withExecutedAt(ZonedDateTime executedAt) {
        this.executedAt = executedAt;
        return this;
    }

    public ScheduleTransaction build() {
        return new ScheduleTransaction(
                id,
                sourceBank,
                sourceAccountId,
                targetBank,
                destinationAccountId,
                amount,
                status,
                failureReason,
                scheduleDt,
                reservedAt,
                executedAt,
                memo
        );
    }
}
