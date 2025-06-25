package tally.transfer.account.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BankCode {
    TOSS_BANK("토스뱅크"),
    KAKAO_BANK("카카오뱅크"),
    KAKAO_PAY("카카오페이"),
    NH_BANK("농협은행"),
    TALLY_BANK("탈리뱅크"),
    ;

    private final String description;
}
