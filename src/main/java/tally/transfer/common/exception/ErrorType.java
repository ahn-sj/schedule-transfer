package tally.transfer.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorType {

    LIMIT_EXCEEDED("요청 횟수 초과"),
    ;

    private final String description;
}
