package tally.transfer.transaction.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.Assert;
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
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "schedule_transaction_id")
    private Long id;

    @Column(name = "source_account_id", nullable = false)
    private Long source;

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

    private ScheduleTransaction(
            final Long source,
            final Long destination,
            final Money amount,
            final LocalDate scheduleDt,
            final String memo
    ) {
        Assert.notNull(source, "송신자 계좌 ID는 null일 수 없습니다.");
        Assert.notNull(destination, "수신자 계좌 ID는 null일 수 없습니다.");
        Assert.notNull(amount, "거래 금액은 null일 수 없습니다.");
        Assert.notNull(scheduleDt, "예약 날짜는 null일 수 없습니다.");
        Assert.isTrue(!scheduleDt.isBefore(LocalDate.now()), "예약 날짜는 오늘 또는 이후여야 합니다.");

        this.source = source;
        this.destination = destination;
        this.amount = amount;
        this.status = ScheduleTransactionStatus.RESERVED;
        this.scheduleDt = scheduleDt;
        this.memo = memo;
    }

    public static ScheduleTransaction schedule(
            final Long source,
            final Long destination,
            final Money amount,
            final LocalDate scheduleDt
    ) {
        return new ScheduleTransaction(source, destination, amount, scheduleDt, null);
    }

    public static ScheduleTransaction schedule(
            final Long source,
            final Long destination,
            final Money amount,
            final LocalDate scheduleDt,
            final String memo
    ) {
        return new ScheduleTransaction(source, destination, amount, scheduleDt, memo);
    }
}
