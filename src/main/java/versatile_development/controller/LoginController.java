package versatile_development.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import versatile_development.constants.Constants;
import versatile_development.domain.Role;
import versatile_development.domain.UserDTO;
import versatile_development.service.EmailService;
import versatile_development.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
public class LoginController {

    @Autowired
    UserService userService;

    @Autowired
    private EmailService emailService;

    @Value("${host.url}")
    String hostUrl;

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@Valid @RequestBody UserDTO userDTO){
        if (userService.userExists(userDTO)){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        else{
            userDTO.setEmail(userDTO.getEmail().toLowerCase());
            userDTO.setActivated(false);
            userDTO.setConfirmationToken(UUID.randomUUID().toString());
            userDTO.setRoles(Collections.singleton(Role.USER));
            userService.createUser(userDTO);

            String message = String.format(Constants.EMAIL_MESSAGE, userDTO.getNickname(), hostUrl, userDTO.getConfirmationToken());

            emailService.sendEmail(userDTO.getEmail(), userDTO.getConfirmationToken(), message);

            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<UserDTO> getAllUsers(){
        return userService.findAllUsers();
    }
}
