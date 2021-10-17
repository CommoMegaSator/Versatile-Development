package versatile_development.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import versatile_development.domain.dto.UserDTO;
import versatile_development.service.UserService;

import javax.validation.Valid;

@Slf4j
@RestController
public class LoginController {

    private UserService userService;

    @Autowired
    LoginController(@Qualifier(value = "userServiceImpl") UserService userService){
        this.userService = userService;
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@Valid @RequestBody UserDTO userDTO){
        if (userService.userExists(userDTO)){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        else{
            HttpStatus httpStatus = userService.register(userDTO);
            return new ResponseEntity<>(httpStatus);
        }
    }
}
