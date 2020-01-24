package App.repository;

import App.domain.UserDTO;
import App.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findUserByNicknameIgnoreCase(String username);
    UserEntity findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
}