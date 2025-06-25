package tally.transfer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tally.transfer.transaction.domain.policy.ScheduleAmountPolicy;
import tally.transfer.user.domain.enums.UserGrade;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class StrategyConfig {

    @Bean
    public Map<UserGrade, ScheduleAmountPolicy> getScheduleAmountPolicyMap(
            final List<ScheduleAmountPolicy> scheduleAmountPolicies
    ) {
        return scheduleAmountPolicies.stream()
                .collect(Collectors.toMap(ScheduleAmountPolicy::getGrade, policy -> policy));
    }
}
