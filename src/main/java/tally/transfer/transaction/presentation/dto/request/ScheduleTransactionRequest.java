package tally.transfer.transaction.presentation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tally.transfer.account.domain.enums.BankCode;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleTransactionRequest {
    private BankCode sourceBank;
    private String source;
    private BankCode targetBank;
    private String target;
    private Long amount;
    private LocalDate scheduleDt;
    private String memo;
}
