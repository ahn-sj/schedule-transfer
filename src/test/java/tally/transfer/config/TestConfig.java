package tally.transfer.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import tally.transfer.transaction.domain.repository.ScheduleTransactionRepository;
import tally.transfer.transaction.stub.StubScheduleTransactionRepository;
import tally.transfer.user.domain.repository.UserRepository;
import tally.transfer.user.stub.StubUserRepository;

@TestConfiguration
public class TestConfig {

    @Bean
    public UserRepository userRepository() {
        return new StubUserRepository();
    }

    @Bean
    public ScheduleTransactionRepository scheduleTransactionRepository() {
        return new StubScheduleTransactionRepository();
    }
}
