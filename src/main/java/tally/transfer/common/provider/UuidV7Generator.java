package tally.transfer.common.provider;

import com.github.f4b6a3.uuid.UuidCreator;

import java.util.UUID;

public class UuidV7Generator implements IdentifierGenerator {

    @Override
    public UUID generate() {
        return UuidCreator.getTimeOrderedEpoch();
    }
}
