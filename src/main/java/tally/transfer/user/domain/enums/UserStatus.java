package tally.transfer.user.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserStatus {
    ACTIVE("활성화"),
    DORMANT("휴면"),
    DEACTIVATED("탈퇴처리"),
    ;

    private final String description;

    public boolean canDeactivate() {
        return this == ACTIVE || this == DORMANT;
    }
}
