package App.controller;

import App.domain.UserDTO;
import App.service.EmailService;
import App.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

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
            userService.createUser(userDTO);

            String message = "Hello " + userDTO.getNickname() + "! Welcome to Versatile family;)\n" +
                    "To confirm your e-mail address, please click the link below:\n"
                    + hostUrl + "/confirm?token=" + userDTO.getConfirmationToken();
            emailService.sendEmail(userDTO.getEmail(), userDTO.getConfirmationToken(), message);

            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

    @GetMapping("/all")
    public List<UserDTO> getAllUsers(){
        return userService.findAllUsers();
    }
}
