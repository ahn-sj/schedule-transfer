package tally.transfer.user.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tally.transfer.common.exception.ErrorCode;
import tally.transfer.user.domain.User;
import tally.transfer.user.domain.repository.UserRepository;
import tally.transfer.user.exception.UserException;
import tally.transfer.user.persistence.repository.UserJpaRepository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User save(final User user) {
        return userJpaRepository.save(user);
    }

    @Override
    public Optional<User> findById(final Long id) {
        return userJpaRepository.findById(id);
    }

    @Override
    public void checkExists(final Long userId) {
        if (!userJpaRepository.existsById(userId)) {
            throw new UserException.UserNotFoundException(ErrorCode.USER_NOT_EXISTS, "존재하지 않는 사용자입니다. userId = " + userId);
        }
    }
}
