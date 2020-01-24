package App.controller;

import App.domain.UserDTO;
import App.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LoginController {

    @Autowired
    UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody UserDTO userDTO){
        if (isUserExists(userDTO)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        else{
            userService.createUser(userDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> signUp(@RequestParam("login") String login, @RequestParam("password") String password){
        UserDTO userDTO = userService.findUserByNickname(login);

        if (userDTO == null){
            userDTO = userService.findByEmail(login);
        }
        if (userDTO != null && userDTO.getPassword().equals(passwordEncoder.encode(password))){
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/all")
    public List<UserDTO> getAllUsers(){
        return userService.findAllUsers();
    }

    public boolean isUserExists(UserDTO userDTO){
        for(UserDTO user : userService.findAllUsers()){
            if (user.getEmail().equals(userDTO.getEmail()) || user.getNickname().equals(userDTO.getNickname()))return true;
        }return false;
    }
}
