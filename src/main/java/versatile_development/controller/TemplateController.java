package versatile_development.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import versatile_development.constants.Constants;
import versatile_development.domain.dto.IntermediateUser;
import versatile_development.domain.dto.UserDTO;
import versatile_development.entity.UserEntity;
import versatile_development.service.UserService;

import java.text.SimpleDateFormat;

@Slf4j
@Controller
@RequestMapping("/")
@Tag(name="Template Controller", description="Контроллер для видачі генерованих HTML сторінок")
public class TemplateController {

    UserService userService;

    @Autowired
    TemplateController(@Qualifier(value = "userServiceImpl") UserService userService){
        this.userService = userService;
    }

    @GetMapping("login")
    @Operation(summary = "Логін панель", description = "Повертає сторінку авторизації")
    public String getLoginView(@AuthenticationPrincipal UserEntity userEntity){
        if (userEntity != null)return "redirect:/profile";
        return "login";
    }

    @GetMapping("registration")
    @Operation(summary = "Панель реєстрації", description = "Повертає сторінку реєстрації")
    public String getRegistrationView(@AuthenticationPrincipal UserEntity userEntity){
        if (userEntity != null)return "redirect:/profile";
        return "registration";
    }

    @GetMapping("confirm")
    @Operation(summary = "Активація акаунта", description = "Активовує акаунт")
    public String activateAccount(@RequestParam String token){
        var userDTO = userService.findByConfirmationToken(token);
        if (userDTO != null) {
            userDTO.setActivated(true);
            userDTO.setConfirmationToken(null);
            userDTO.setTokenExpiration(null);
            userService.updateUser(userDTO);

            log.info(userDTO.getNickname() + " confirmed registration.");
        }
        return "redirect:/login";
    }

    @GetMapping
    @Operation(summary = "Головна сторінка", description = "Повертає головну сторінку")
    public String getMainPageView(){
        return "main";
    }

    @GetMapping("profile")
    @PreAuthorize("hasAnyAuthority('ADMIN, USER')")
    @Operation(summary = "Профіль користувача", description = "Повертає сторінку користувача")
    public String getProfileView(@AuthenticationPrincipal UserEntity user,
                                 @RequestParam(name = "nickname", required = false) String nickname, Model model){
        var DateFor = new SimpleDateFormat("dd.MM.yyyy");
        UserDTO userDTO;
        if (nickname != null) {
            userDTO = userService.findByNickname(nickname);
            if (userDTO != null)
                model.addAttribute("user", userDTO);
        }
        else {
            userDTO = userService.findByNickname(user.getNickname());
            model.addAttribute("user", userDTO);
        }
        if (userDTO.getBirthday() != null)model.addAttribute("userBirthday", DateFor.format(userDTO.getBirthday()));
        return "profile";
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Сторінка користувачів", description = "Повертає сторінку з даними всіх користувачів")
    public String getAllUsers(Model model){
        model.addAttribute("users", userService.findAllUsers(Sort.by("id")));
        return "all_users";
    }

    @DeleteMapping("/all")
    @Operation(summary = "Видалення користувача", description = "Видаляє акаунт обраного користувача")
    public ResponseEntity deleteUser(@RequestBody IntermediateUser user, @AuthenticationPrincipal UserEntity adminThatDeleting){
        if (user.getNickname().equals(adminThatDeleting.getNickname())){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        if (user.getNickname() == null || !user.getToken().equals(Constants.DELETING_USER_TOKEN) || user.getNickname().equals(Constants.CREATOR_NICKNAME)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (user.getNickname() != null){
            userService.deleteAccountByNickname(user.getNickname());
        }
        log.info(user.getNickname() + "`s account was deleted by " + adminThatDeleting.getNickname());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/messages")
    @Operation(summary = "Сторінка повідомлень", description = "Повертає сторінку з повідомленнями")
    public String getMessagePage(){
        return "messages";
    }

    @GetMapping("/friends")
    @Operation(summary = "Сторінка друзів", description = "Повертає сторінку з друзями користувача")
    public String getFriendsTemplate(){
        return "friends";
    }
}
