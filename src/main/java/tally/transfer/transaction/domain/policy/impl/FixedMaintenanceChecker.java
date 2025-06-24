package tally.transfer.transaction.domain.policy.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tally.transfer.common.domain.vo.TimeWindow;
import tally.transfer.transaction.domain.policy.MaintenanceChecker;

import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class FixedMaintenanceChecker implements MaintenanceChecker {

    private final TimeWindow maintenanceWindow;

    public FixedMaintenanceChecker() {
        this.maintenanceWindow = TimeWindow.between(
                LocalTime.of(23, 30),
                LocalTime.of(0, 30)
        );
    }

    @Override
    public boolean isNowInMaintenanceWindow() {
        // TODO: 시간 대역을 설정할 수 있도록 개선 필요
        return maintenanceWindow.isWithin(LocalTime.now());
    }
}
