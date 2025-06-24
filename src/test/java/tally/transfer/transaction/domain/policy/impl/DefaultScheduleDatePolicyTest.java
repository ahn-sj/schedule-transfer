package tally.transfer.transaction.domain.policy.impl;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import tally.transfer.common.stub.StubDateProvider;
import tally.transfer.transaction.domain.exception.InvalidScheduleDateException;
import tally.transfer.transaction.domain.policy.ScheduleDatePolicy;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DefaultScheduleDatePolicyTest {

    @ParameterizedTest(name = "[성공] 예약 날짜는 오늘 또는 이후여야 한다. input = {0}")
    @MethodSource("validDates")
    void validate(final LocalDate scheduleDt) {
        // given:
        final LocalDate today = LocalDate.of(2025, 1, 1);
        final ScheduleDatePolicy policy = new DefaultScheduleDatePolicy(new StubDateProvider(today));

        // when:
        // then:
        policy.validate(scheduleDt);
    }

    private static List<LocalDate> validDates() {
        return List.of(
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 1, 2)
        );
    }

    @ParameterizedTest(name = "[실패] 예약 날짜는 오늘 또는 이후여야 한다. input = {0}")
    @MethodSource("invalidDates")
    void validateFail(final LocalDate scheduleDt) {
        // given:
        final LocalDate today = LocalDate.of(2025, 1, 1);
        final ScheduleDatePolicy policy = new DefaultScheduleDatePolicy(new StubDateProvider(today));

        // when:
        // then:
        assertThatThrownBy(() -> policy.validate(scheduleDt))
                .isInstanceOf(InvalidScheduleDateException.class);
    }

    private static List<LocalDate> invalidDates() {
        return List.of(
                LocalDate.of(2024, 12, 30),
                LocalDate.of(2024, 12, 31)
        );
    }

}