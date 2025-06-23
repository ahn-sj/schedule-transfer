package tally.transfer.user.domain;

import tally.transfer.user.domain.enums.UserGrade;
import tally.transfer.user.domain.enums.UserStatus;

public class UserFixture {

    private Long id = null;
    private String name = "기본이름";
    private UserGrade grade = UserGrade.BASIC;
    private UserStatus status = UserStatus.ACTIVE;

    public static UserFixture aUser() {
        return new UserFixture();
    }

    public UserFixture withId(Long id) {
        this.id = id;
        return this;
    }

    public UserFixture withName(String name) {
        this.name = name;
        return this;
    }

    public UserFixture withGrade(UserGrade grade) {
        this.grade = grade;
        return this;
    }

    public UserFixture withStatus(UserStatus status) {
        this.status = status;
        return this;
    }

    public User build() {
        return new User(id, name, grade, status);
    }
}
