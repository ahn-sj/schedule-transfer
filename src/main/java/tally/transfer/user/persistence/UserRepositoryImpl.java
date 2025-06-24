package tally.transfer.user.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tally.transfer.user.domain.User;
import tally.transfer.user.domain.repository.UserRepository;
import tally.transfer.user.persistence.repository.UserJpaRepository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository repository;

    @Override
    public User save(final User user) {
        return repository.save(user);
    }

    @Override
    public Optional<User> findById(final Long id) {
        return repository.findById(id);
    }
}
