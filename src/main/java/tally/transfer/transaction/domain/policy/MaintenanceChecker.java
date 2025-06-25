package tally.transfer.transaction.domain.policy;

public interface MaintenanceChecker {
    boolean isNowInMaintenanceWindow();
    String getMaintenanceWindow();
}
