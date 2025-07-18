package tally.transfer.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorType {

    LIMIT_EXCEEDED("요청 횟수 초과"),
    NOT_EXISTS("존재하지 않는 리소스"),
    MAINTENANCE("점검 중"),
    INVALID_REQUEST("잘못된 요청"),
    CONCURRENT_MODIFICATION("동시 처리 충돌"),
    REDIS_FAILURE("Redis 처리 실패"),
    ;

    private final String description;
}
