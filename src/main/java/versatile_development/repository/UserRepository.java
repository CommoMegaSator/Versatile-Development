package versatile_development.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import versatile_development.entity.UserEntity;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByNickname(String data);
    UserEntity findByConfirmationToken(String confirmationToken);
    UserEntity findByNickname(String nickname);
    UserEntity findByNicknameIgnoreCase(String nickname);
    void deleteByNickname(String nickname);

    @Query("SELECT user FROM UserEntity user " +
            "WHERE EXTRACT(day FROM birthday) = :day AND EXTRACT(month FROM birthday) = :month")
    List<UserEntity> findAllByBirthday(@Param("day") Integer day, @Param("month") Integer month);

    @Query(value = "SELECT e FROM UserEntity e WHERE e.tokenExpiration < CURRENT_DATE")
    List<UserEntity> findAllByTokenExpirationLessThanCurrentTime();
}