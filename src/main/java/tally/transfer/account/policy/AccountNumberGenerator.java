package tally.transfer.account.policy;

public interface AccountNumberGenerator {
    /**
     * 계좌 번호를 생성합니다.
     *
     * @return 생성된 계좌 번호
     */
    String generate();
}
