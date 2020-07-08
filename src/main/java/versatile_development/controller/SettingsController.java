package versatile_development.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import versatile_development.domain.Role;
import versatile_development.domain.dto.UserDTO;
import versatile_development.domain.dto.UserForUpdating;
import versatile_development.entity.UserEntity;
import versatile_development.exception.EmptyUserDataException;
import versatile_development.service.UserService;

@Slf4j
@Controller
@RequestMapping("/settings")
public class SettingsController {

    UserService userService;

    @Autowired
    SettingsController(@Qualifier(value = "userServiceImpl") UserService userService){
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN, USER')")
    public String getSettings(@AuthenticationPrincipal UserEntity user, Model model){
        UserDTO userDTO = userService.findByNickname(user.getNickname());
        boolean isAdmin = user.getAuthorities().contains(Role.ADMIN);

        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("user", userDTO);
        return "settings";
    }

    @DeleteMapping
    public ResponseEntity deleteAccount(@AuthenticationPrincipal UserEntity userEntity){
        userService.deleteAccountByNickname(userEntity.getNickname());
        log.info(userEntity.getNickname() + " account was deleted by himself.");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN, USER')")
    public ResponseEntity updateUserInfo(@RequestBody UserForUpdating user, @AuthenticationPrincipal UserEntity userEntity){
        try {
            userService.updateUserInformationFromSettings(user, userEntity.getNickname());
        }catch (EmptyUserDataException e){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
