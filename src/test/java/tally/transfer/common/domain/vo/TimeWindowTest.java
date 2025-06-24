package tally.transfer.common.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TimeWindowTest {

    @Test
    @DisplayName("[성공] 주어진 시간이 시작과 종료 사이에 있는 경우 true를 반환한다")
    void isWithin_basic_success() {
        // given:
        TimeWindow window = TimeWindow.between(LocalTime.of(9, 0), LocalTime.of(18, 0));

        // when & then:
        assertThat(window.isWithin(LocalTime.of(10, 0))).isTrue(); // 중간
        assertThat(window.isWithin(LocalTime.of(9, 0))).isTrue();  // 시작 시간
        assertThat(window.isWithin(LocalTime.of(17, 59))).isTrue(); // 종료 직전
    }

    @Test
    @DisplayName("[실패] 주어진 시간이 시간 범위 밖인 경우 false를 반환한다")
    void isWithin_basic_fail() {
        // given:
        TimeWindow window = TimeWindow.between(LocalTime.of(9, 0), LocalTime.of(18, 0));

        // when & then:
        assertThat(window.isWithin(LocalTime.of(8, 59))).isFalse();
        assertThat(window.isWithin(LocalTime.of(18, 0))).isFalse(); // 종료 시각 포함 안함
        assertThat(window.isWithin(LocalTime.of(23, 0))).isFalse();
    }

    @Test
    @DisplayName("[성공] 종료 시간이 시작 시간보다 이른 경우(자정 넘김) 올바르게 판단한다")
    void isWithin_overMidnight_success() {
        // given:
        TimeWindow window = TimeWindow.between(LocalTime.of(23, 0), LocalTime.of(2, 0));

        // when & then:
        assertThat(window.isWithin(LocalTime.of(23, 0))).isTrue(); // 시작 시각
        assertThat(window.isWithin(LocalTime.of(0, 0))).isTrue();  // 자정
        assertThat(window.isWithin(LocalTime.of(1, 59))).isTrue(); // 종료 직전
    }

    @Test
    @DisplayName("[실패] 자정 넘김 시간 범위에 포함되지 않는 경우 false를 반환한다")
    void isWithin_overMidnight_fail() {
        // given:
        TimeWindow window = TimeWindow.between(LocalTime.of(23, 0), LocalTime.of(2, 0));

        // when & then:
        assertThat(window.isWithin(LocalTime.of(2, 0))).isFalse(); // 종료 시각
        assertThat(window.isWithin(LocalTime.of(10, 0))).isFalse(); // 아침
        assertThat(window.isWithin(LocalTime.of(22, 59))).isFalse(); // 시작 전
    }

    @Test
    @DisplayName("[예외] null 시작 또는 종료 시간은 예외 발생")
    void constructor_shouldThrow_whenNullProvided() {
        assertThatThrownBy(() -> new TimeWindow(null, LocalTime.of(12, 0)))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> new TimeWindow(LocalTime.of(9, 0), null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("[성공] equals, hashCode 동작 확인")
    void equalsAndHashCode() {
        // given:
        TimeWindow window1 = TimeWindow.between(LocalTime.of(9, 0), LocalTime.of(18, 0));
        TimeWindow window2 = TimeWindow.between(LocalTime.of(9, 0), LocalTime.of(18, 0));

        // then:
        assertThat(window1).isEqualTo(window2);
        assertThat(window1.hashCode()).isEqualTo(window2.hashCode());
    }

    @Test
    @DisplayName("[성공] toString 형식 확인")
    void toString_check() {
        TimeWindow window = TimeWindow.between(LocalTime.of(9, 0), LocalTime.of(18, 0));
        assertThat(window.toString()).isEqualTo("[09:00 ~ 18:00]");
    }
}