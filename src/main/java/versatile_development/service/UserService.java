package versatile_development.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import versatile_development.domain.dto.UserDTO;
import versatile_development.entity.UserEntity;

import java.util.List;

public interface UserService extends UserDetailsService {

    void createUser(UserDTO userDTO);
    void updateUser(UserDTO userToUpdate);

    List<UserDTO> findAllUsers();
    UserDTO findByEmail(String email);
    UserDTO findByConfirmationToken(String confirmationToken);
    UserDTO findByNickname(String nickname);

    boolean userExists(UserDTO userDTO);

    UserDTO entityToDTOMapper(UserEntity userEntity);
    UserEntity DTOToEntityMapper(UserDTO userDTO);

    void deleteAccountByNickname(String nickname);
}