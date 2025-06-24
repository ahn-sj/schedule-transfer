package tally.transfer.user.application;

import org.junit.jupiter.api.Test;
import tally.transfer.user.domain.User;
import tally.transfer.user.stub.StubUserRepository;

import static org.assertj.core.api.Assertions.assertThat;

class UserCommandServiceTest {

    @Test
    void createUser() {
        // given:
        final StubUserRepository repository = new StubUserRepository();
        final UserCommandService service = new UserCommandService(repository);

        // when:
        final Long userId = service.createUser("testUser");

        // then:
        final User user = repository.findById(userId).get();
        assertThat(user).isNotNull();
    }

}