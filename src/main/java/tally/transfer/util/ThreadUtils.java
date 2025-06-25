package tally.transfer.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadUtils {

    private ThreadUtils() {}

    public static void delay(final long interval) {
        try {
            Thread.sleep(interval);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Delay 중 인터럽트 발생: {}", e.getMessage(), e);
            throw new RuntimeException("Delay 중 인터럽트 발생", e);
        }
    }
}
