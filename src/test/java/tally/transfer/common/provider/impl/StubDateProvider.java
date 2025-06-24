package tally.transfer.common.provider.impl;

import org.springframework.stereotype.Component;
import tally.transfer.common.provider.DateProvider;

import java.time.LocalDate;
import java.util.Objects;

@Component
public class StubDateProvider implements DateProvider {

    private LocalDate fixedDate = LocalDate.of(2099, 10, 1);

    public StubDateProvider() {}

    public StubDateProvider(final LocalDate fixedDate) {
        this.fixedDate = Objects.requireNonNull(fixedDate);
    }

    @Override
    public LocalDate today() {
        return fixedDate;
    }
}
