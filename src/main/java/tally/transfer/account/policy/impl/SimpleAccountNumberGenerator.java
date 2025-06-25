package tally.transfer.account.policy.impl;

import org.springframework.stereotype.Component;
import tally.transfer.account.policy.AccountNumberGenerator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class SimpleAccountNumberGenerator implements AccountNumberGenerator {

    private final AtomicInteger counter = new AtomicInteger(1);
    private final DateTimeFormatter PREFIX_PATTERN = DateTimeFormatter.ofPattern("yyMMdd");

    /**
     * 계좌 번호를 생성합니다.
     * - 계좌 번호는 현재 날짜를 기준으로 "yyMMdd" 형식의 접두사와 6자리의 순차 번호로 구성됩니다.
     * - 예시: "240101000001" (2024년 1월 1일에 생성된 첫 번째 계좌)
     *
     * @return 생성된 계좌 번호
     */
    @Override
    public String generate() {
        final LocalDateTime now = LocalDateTime.now();
        final String datePrefix = now.format(PREFIX_PATTERN);
        final int sequence = counter.getAndIncrement() % 1_000_000;

        return String.format("%s%06d", datePrefix, sequence);
    }
}
