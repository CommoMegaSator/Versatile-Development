package App.service;

import App.domain.UserDTO;
import java.util.List;

public interface UserService {

    void createUser(UserDTO userDTO);

    List<UserDTO> findAllUsers();

    UserDTO findByEmail(String email);

    UserDTO findByConfirmationToken(String confirmationToken);

    void updateUser(UserDTO userToUpdate);

    boolean userExists(UserDTO userDTO);
}