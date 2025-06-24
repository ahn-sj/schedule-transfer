package tally.transfer.user.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tally.transfer.user.domain.User;

public interface UserJpaRepository extends JpaRepository<User, Long> {
}
