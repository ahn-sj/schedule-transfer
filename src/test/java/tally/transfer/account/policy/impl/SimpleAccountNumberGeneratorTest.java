package tally.transfer.account.policy.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tally.transfer.account.policy.AccountNumberGenerator;

import static org.assertj.core.api.Assertions.assertThat;

class SimpleAccountNumberGeneratorTest {

    @Test
    @DisplayName("[성공] 간편 계좌 생성을 하면 12자리 계좌 번호가 생성된다.")
    void generate() {
        // given:
        final AccountNumberGenerator generator = new SimpleAccountNumberGenerator();

        // when:
        final String accountNumber = generator.generate();

        // then:
        assertThat(accountNumber.length()).isEqualTo(12);
    }
}