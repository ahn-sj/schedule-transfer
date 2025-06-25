package tally.transfer.transaction.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tally.transfer.account.domain.Account;
import tally.transfer.account.domain.repository.AccountRepository;
import tally.transfer.account.domain.vo.Money;
import tally.transfer.common.exception.ErrorCode;
import tally.transfer.transaction.domain.ScheduleTransaction;
import tally.transfer.transaction.domain.exception.BankMaintenanceException;
import tally.transfer.transaction.domain.exception.TransactionException;
import tally.transfer.transaction.domain.policy.MaintenanceChecker;
import tally.transfer.transaction.domain.policy.ScheduleAmountPolicy;
import tally.transfer.transaction.domain.policy.ScheduleDatePolicy;
import tally.transfer.transaction.domain.repository.ScheduleTransactionRepository;
import tally.transfer.transaction.presentation.dto.request.ScheduleTransactionRequest;
import tally.transfer.user.domain.User;
import tally.transfer.user.domain.enums.UserGrade;
import tally.transfer.user.domain.repository.UserRepository;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class TransactionCommandService {

    private final ScheduleTransactionRepository scheduleTransactionRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    private final MaintenanceChecker maintenanceChecker;
    private final ScheduleDatePolicy scheduleDatePolicy;

    private final Map<UserGrade, ScheduleAmountPolicy> scheduleAmountPolicyMap;

    public TransactionCommandService(
            final ScheduleTransactionRepository scheduleTransactionRepository,
            final AccountRepository accountRepository, final UserRepository userRepository,
            final MaintenanceChecker maintenanceChecker, final ScheduleDatePolicy scheduleDatePolicy, final ScheduleAmountPolicy scheduleAmountPolicy, final Map<UserGrade, ScheduleAmountPolicy> scheduleAmountPolicyMap
    ) {
        this.scheduleTransactionRepository = Objects.requireNonNull(scheduleTransactionRepository);
        this.accountRepository = Objects.requireNonNull(accountRepository);
        this.userRepository = Objects.requireNonNull(userRepository);
        this.maintenanceChecker = Objects.requireNonNull(maintenanceChecker);
        this.scheduleDatePolicy = Objects.requireNonNull(scheduleDatePolicy);
        this.scheduleAmountPolicyMap = Objects.requireNonNull(scheduleAmountPolicyMap);
    }

    /**
     * 예약 거래를 생성합니다.
     *
     * @param request 스케줄된 거래 요청
     */
    public void scheduleTransaction(final ScheduleTransactionRequest request) {
        // 은행 점검 여부 확인
        if(maintenanceChecker.isNowInMaintenanceWindow()) {
            throw new BankMaintenanceException(ErrorCode.BANK_MAINTENANCE_ERROR, "은행 점검 중입니다. 잠시 후 다시 시도해주세요. 점검 시간 = " + maintenanceChecker.getMaintenanceWindow());
        }

        // 예약 날짜 유효성 검사
        scheduleDatePolicy.validate(request.getScheduleDt());

        final Account source = accountRepository.getByBankAndAccountNumber(request.getSourceBank(), request.getSource());
        final Account target = accountRepository.getByBankAndAccountNumber(request.getTargetBank(), request.getTarget());

        // 송금 계좌와 입금 계좌가 동일한지 검사
        if(source.isSame(target)) {
            throw new TransactionException.SameAccountException(ErrorCode.SAME_ACCOUNT_ERROR, "송금 계좌와 입금 계좌가 동일합니다. source = " + request.getSource() + ", target = " + request.getTarget());
        }
        final User sender = userRepository.getById(source.getUserId());

        // 1회 최대 예약 송금 금액 유효성 검사
        final ScheduleAmountPolicy amountPolicy = scheduleAmountPolicyMap.get(sender.getGrade());

        final Money amount = Money.wons(request.getAmount());
        if (!amountPolicy.isAllowed(amount)) {
            throw new TransactionException.ScheduleAmountExceedException(ErrorCode.SCHEDULE_AMOUNT_EXCEED_ERROR, "예약 거래 금액이 허용 범위를 초과했습니다. amount = " + request.getAmount());
        };

        final ScheduleTransaction scheduleTransaction = ScheduleTransaction.schedule(
                source.getBankCode(),
                source.getId(),
                target.getBankCode(),
                target.getId(),
                amount,
                request.getScheduleDt()
        );
        final ScheduleTransaction scheduled = scheduleTransactionRepository.save(scheduleTransaction);
    }

}
