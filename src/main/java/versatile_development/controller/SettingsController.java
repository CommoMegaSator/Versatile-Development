package versatile_development.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import versatile_development.domain.Role;
import versatile_development.entity.UserEntity;
import versatile_development.service.UserService;

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
        boolean isAdmin = user.getAuthorities().contains(Role.ADMIN);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("user", user);
        return "settings";
    }

    @DeleteMapping
    public ResponseEntity deleteAccount(@AuthenticationPrincipal UserEntity userEntity){
        userService.deleteAccountByNickname(userEntity.getNickname());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
