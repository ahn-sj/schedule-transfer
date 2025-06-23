package tally.transfer.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;
import tally.transfer.user.domain.enums.UserGrade;
import tally.transfer.user.domain.enums.UserStatus;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private UserGrade grade;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    private User(final String name) {
        this.name = name;
        this.grade = UserGrade.BASIC;
        this.status = UserStatus.ACTIVE;
    }

    public static User create(final String name) {
        return new User(name);
    }

    public void deactivate() {
        Assert.state(status.canDeactivate(), "탈퇴는 활성화 또는 휴면 상태일 때만 가능합니다. status = " + status);
        this.status = UserStatus.DEACTIVATED;
    }

    public void reactivate() {
        Assert.state(status == UserStatus.DORMANT, "휴면 해제는 휴면 상태일 때만 가능합니다. status = " + status);
        this.status = UserStatus.ACTIVE;
    }
}
