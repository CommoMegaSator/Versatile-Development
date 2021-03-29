package versatile_development.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import versatile_development.constants.Constants;
import versatile_development.domain.Role;
import versatile_development.domain.dto.UserDTO;
import versatile_development.service.EmailService;
import versatile_development.service.UserService;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

@Slf4j
@RestController
public class LoginController {

    private UserService userService;

    private EmailService emailService;

    @Value("${host.url}")
    String hostUrl;

    @Autowired
    LoginController(@Qualifier(value = "userServiceImpl") UserService userService,
                    @Qualifier(value = "emailServiceImpl") EmailService emailService){
        this.userService = userService;
        this.emailService = emailService;
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@Valid @RequestBody UserDTO userDTO){
        if (userService.userExists(userDTO)){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        else{
            Date creationDate = new Date();

            userDTO.setCreationDate(creationDate);
            userDTO.setTokenExpiration(new Date(creationDate.getTime() + Constants.DAY));
            userDTO.setEmail(userDTO.getEmail().toLowerCase());
            userDTO.setActivated(false);
            userDTO.setConfirmationToken(UUID.randomUUID().toString());
            userDTO.setRoles(Collections.singleton(Role.USER));
            userService.createUser(userDTO);

            String message = String.format(Constants.EMAIL_MESSAGE, userDTO.getNickname(), hostUrl, userDTO.getConfirmationToken());
            emailService.sendEmail(userDTO.getEmail(), userDTO.getConfirmationToken(), message);

            log.info(String.format(userDTO.getNickname() + " activation link: " + "%sconfirm?token=%s", hostUrl, userDTO.getConfirmationToken()));

            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }
}
