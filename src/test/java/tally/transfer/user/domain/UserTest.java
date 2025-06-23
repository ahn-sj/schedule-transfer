package tally.transfer.user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tally.transfer.user.domain.enums.UserStatus;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {

    @Test
    @DisplayName("[성공] 사용자 활성화 상태이면 탈퇴가 가능하다")
    void deactivate() {
        // given:
        final User user = UserFixture.aUser()
                .withStatus(UserStatus.ACTIVE)
                .build();

        // when:
        user.deactivate();

        // then:
        assertEquals(UserStatus.DEACTIVATED, user.getStatus());
    }

    @Test
    @DisplayName("[실패] 사용자 탈퇴 상태이면 탈퇴가 불가능하다.")
    void deactivate_fail() {
        // given:
        final User user = UserFixture.aUser()
                .withStatus(UserStatus.DEACTIVATED)
                .build();

        // when:
        // then:
        assertThatThrownBy(user::deactivate)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("[성공] 사용자 휴면 상태이면 휴면 해제가 가능하다")
    void reactivate() {
        // given:
        final User user = UserFixture.aUser()
                .withStatus(UserStatus.DORMANT)
                .build();

        // when:
        user.reactivate();

        // then:
        assertEquals(UserStatus.ACTIVE, user.getStatus());
    }

}