package tally.transfer.transaction.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.Assert;
import tally.transfer.account.domain.enums.BankCode;
import tally.transfer.account.domain.vo.Money;
import tally.transfer.transaction.domain.enums.ScheduleTransactionStatus;
import tally.transfer.transaction.domain.enums.TransactionFailureReason;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Getter
@Entity
@Table(name = "schedule_transactions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@ToString
public class ScheduleTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_transaction_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private BankCode sourceBank;

    @Column(name = "source_account_id", nullable = false)
    private Long source;

    @Enumerated(EnumType.STRING)
    private BankCode destinationBank;

    @Column(name = "destination_account_id", nullable = false)
    private Long destination;

    @Embedded
    private Money amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScheduleTransactionStatus status;

    @Enumerated(EnumType.STRING)
    private TransactionFailureReason failureReason;

    private LocalDate scheduleDt;

    @CreatedDate
    private ZonedDateTime reservedAt;

    private ZonedDateTime executedAt;

    private String memo;

    /**
     * 예약 거래를 생성합니다.
     * - 잔액없더라도 예약 거래 생성은 가능하고, 실행 시점에 잔액이 없으면 실패
     *
     * @param source 송금자 계좌 ID
     * @param destination 수신자 계좌 ID
     * @param amount 거래 금액
     * @param scheduleDt 예약 날짜
     * @param memo 거래 메모 (선택 사항)
     */
    private ScheduleTransaction(
            final BankCode sourceBank,
            final Long source,
            final BankCode destinationBank,
            final Long destination,
            final Money amount,
            final LocalDate scheduleDt,
            final String memo
    ) {
        Assert.notNull(sourceBank, "송신자 은행 코드는 null일 수 없습니다.");
        Assert.notNull(source, "송신자 계좌 ID는 null일 수 없습니다.");
        Assert.notNull(destinationBank, "수신자 은행 코드는 null일 수 없습니다.");
        Assert.notNull(destination, "수신자 계좌 ID는 null일 수 없습니다.");
        Assert.notNull(amount, "거래 금액은 null일 수 없습니다.");
        Assert.notNull(scheduleDt, "예약 날짜는 null일 수 없습니다.");

        this.sourceBank = sourceBank;
        this.source = source;
        this.destinationBank = destinationBank;
        this.destination = destination;
        this.amount = amount;
        this.status = ScheduleTransactionStatus.RESERVED;
        this.scheduleDt = scheduleDt;
        this.memo = memo;
    }

    public static ScheduleTransaction schedule(
            final BankCode sourceBank,
            final Long source,
            final BankCode destinationBank,
            final Long destination,
            final Money amount,
            final LocalDate scheduleDt
    ) {
        return new ScheduleTransaction(sourceBank, source, destinationBank, destination, amount, scheduleDt, null);
    }
}
