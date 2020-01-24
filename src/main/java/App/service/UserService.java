package App.service;

import App.domain.UserDTO;
import App.entity.UserEntity;
import java.util.List;

public interface UserService {

    void createUser(UserDTO userDTO);
    List<UserDTO> findAllUsers();

    UserDTO findUserByNickname(String nickname);

    UserDTO findByEmail(String email);

    boolean emailExist(String email);
}