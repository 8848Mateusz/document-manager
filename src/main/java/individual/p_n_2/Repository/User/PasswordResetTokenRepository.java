package individual.p_n_2.Repository.User;

import individual.p_n_2.Domain.User.PasswordResetToken;
import individual.p_n_2.Domain.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    void deleteByUser(User user);
}
