package tally.transfer.account.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;
import tally.transfer.account.domain.enums.AccountType;
import tally.transfer.account.domain.vo.Money;

@Getter
@Entity
@Table(name = "accounts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private AccountType type;

    @Embedded
    private Money balance;

    private Account(final Long userId, final AccountType type) {
        Assert.notNull(userId, "사용자 ID는 null일 수 없습니다.");
        Assert.notNull(type, "계좌 유형은 null일 수 없습니다.");
        this.userId = userId;
        this.type = type;
        this.balance = Money.ZERO;
    }

    public static Account open(final Long userId, final AccountType type) {
        return new Account(userId, type);
    }

    public boolean canWithdraw(Money amount) {
        return this.balance.isGreaterThan(amount);
    }
}
