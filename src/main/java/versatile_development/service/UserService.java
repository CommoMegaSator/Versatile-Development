package versatile_development.service;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import versatile_development.domain.dto.UserDTO;
import versatile_development.domain.dto.UserForUpdating;
import versatile_development.entity.UserEntity;

import java.util.List;

public interface UserService extends UserDetailsService {

    void updateUser(UserDTO userToUpdate);
    void updateUserInformationFromSettings(UserForUpdating user, String nickname);

    List<UserDTO> findAllUsers(Sort sort);
    UserDTO findByEmail(String email);
    UserDTO findByConfirmationToken(String confirmationToken);
    UserDTO findByNickname(String nickname);

    boolean userExists(UserDTO userDTO);

    UserDTO entityToDTOMapper(UserEntity userEntity);
    UserEntity DTOToEntityMapper(UserDTO userDTO);

    void deleteAccountByNickname(String nickname);
    HttpStatus register(UserDTO userDTO);
}