package tally.transfer.common.provider.impl;

import org.springframework.stereotype.Component;
import tally.transfer.common.provider.DateProvider;

import java.time.Clock;
import java.time.LocalDate;

@Component
public class SystemDateProvider implements DateProvider {

    private final Clock clock;

    public SystemDateProvider(final Clock clock) {
        this.clock = clock;
    }

    @Override
    public LocalDate today() {
        return LocalDate.now(clock);
    }
}
