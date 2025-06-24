package tally.transfer.transaction.domain.policy;

import java.time.LocalDate;

public interface ScheduleDatePolicy {
    void validate(LocalDate scheduleDt);
}
