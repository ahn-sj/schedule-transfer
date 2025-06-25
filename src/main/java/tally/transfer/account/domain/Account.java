package tally.transfer.account.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;
import tally.transfer.account.domain.enums.AccountType;
import tally.transfer.account.domain.enums.BankCode;
import tally.transfer.account.domain.vo.Money;
import tally.transfer.account.policy.AccountNumberGenerator;

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

    @Enumerated(EnumType.STRING)
    private BankCode bankCode;

    @Column(nullable = false, unique = true, length = 20)
    private String accountNumber;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private AccountType type;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "balance"))
    private Money balance;

    private Account(final BankCode bankCode, final String accountNumber, final Long userId, final AccountType type) {
        Assert.notNull(bankCode, "은행 코드는 null일 수 없습니다.");
        Assert.notNull(userId, "사용자 ID는 null일 수 없습니다.");
        Assert.notNull(type, "계좌 유형은 null일 수 없습니다.");
        Assert.hasText(accountNumber, "계좌 번호는 비어 있을 수 없습니다.");

        this.bankCode = bankCode;
        this.accountNumber = accountNumber;
        this.userId = userId;
        this.type = type;
        this.balance = Money.ZERO;
    }

    public static Account open(
            final Long userId,
            final AccountType type,
            final AccountNumberGenerator accountNumberGenerator
    ) {
        return new Account(BankCode.TALLY_BANK, accountNumberGenerator.generate(), userId, type);
    }

    public boolean canWithdraw(Money amount) {
        return this.balance.isGreaterThanOrEqualTo(amount);
    }
}
