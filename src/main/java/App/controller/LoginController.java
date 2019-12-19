package App.controller;

import App.utils.ObjectMapperUtils;
import App.entity.UserEntity;
import App.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

    @Autowired
    UserService userService;

//    @Autowired
//    private ObjectMapperUtils modelMapper;

    public boolean isUserExists(UserEntity userEntity){
        for(UserEntity user : userService.findAllUsers()){
            if (user.getEmail().equals(userEntity.getEmail()) || user.getNickname().equals(userEntity.getNickname()))return true;
        }return false;
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody UserEntity userEntity){
        //UserEntity userEntity = modelMapper.map(userDTO, UserEntity.class);
        if (isUserExists(userEntity)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        else{
            userService.createUser(userEntity);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> signUp(@RequestParam("login") String nickname, @RequestParam("password") String password){
        UserEntity userEntity = userService.findUserByNickname(nickname);

        if (userEntity == null){
            userEntity = userService.findByEmail(nickname);
        }
        if (userEntity != null && userEntity.getPassword().equals(password)){
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
