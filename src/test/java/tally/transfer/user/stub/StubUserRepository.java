package tally.transfer.user.stub;

import org.springframework.stereotype.Repository;
import tally.transfer.user.domain.User;
import tally.transfer.user.domain.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class StubUserRepository implements UserRepository {

    private final Map<Long, User> store = new HashMap<>();
    private final AtomicLong id = new AtomicLong(1L);

    @Override
    public User save(final User user) {
        final long userId = this.id.getAndIncrement();
        final User newUser = new User(userId, user.getName(), user.getGrade(), user.getStatus());
        store.put(userId, newUser);

        return newUser;
    }

    @Override
    public Optional<User> findById(final Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public User getById(final Long userId) {
        return Optional.ofNullable(store.get(userId))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다. userId = " + userId));
    }

    @Override
    public void checkExists(final Long userId) {
        if (!store.containsKey(userId)) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다. userId = " + userId);
        }
    }
}
