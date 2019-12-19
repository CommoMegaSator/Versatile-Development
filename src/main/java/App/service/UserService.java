package App.service;

import App.entity.UserEntity;
import java.util.List;

public interface UserService {

    void createUser(UserEntity userEntity);
    List<UserEntity> findAllUsers();

    UserEntity findUserByNickname(String nickname);

    UserEntity findByEmail(String email);
}