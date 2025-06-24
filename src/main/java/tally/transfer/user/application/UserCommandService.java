package tally.transfer.user.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tally.transfer.user.domain.User;
import tally.transfer.user.domain.repository.UserRepository;

import java.util.Objects;

@Slf4j
@Service
public class UserCommandService {

    private final UserRepository userRepository;

    public UserCommandService(final UserRepository userRepository) {
        this.userRepository = Objects.requireNonNull(userRepository);
    }

    public Long createUser(final String name) {
        final User user = User.create(name);
        final User savedUser = userRepository.save(user);

        return savedUser.getId();
    }


}
