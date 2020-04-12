package versatile_development.repository;

import versatile_development.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByNickname(String data);
    UserEntity findByConfirmationToken(String confirmationToken);
    UserEntity findByNickname(String nickname);
    void deleteByNickname(String nickname);
}