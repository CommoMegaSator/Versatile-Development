package versatile_development.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import versatile_development.domain.dto.UserForUpdating;
import versatile_development.entity.UserEntity;
import versatile_development.exception.EmptyUserDataException;
import versatile_development.service.UserService;

import java.util.Calendar;

import static versatile_development.constants.Constants.MIN_YEARS_OLD_MINUS;
import static versatile_development.constants.Constants.START_DATE_LIMIT;

@Slf4j
@Controller
@RequestMapping("/settings")
@Tag(name="Settings Controller", description="Контроллер для налаштування акаунта")
public class SettingsController {

    private final UserService userService;

    @Autowired
    SettingsController(@Qualifier(value = "userServiceImpl") UserService userService){
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN, USER')")
    @Operation(summary = "Отримання сторінки", description = "Дозволяє отримати сторінку налаштувань")
    public String getSettings(@AuthenticationPrincipal UserEntity user, Model model){
        var userDTO = userService.findByNickname(user.getNickname());
        var isAdmin = user.getAuthorities().contains(Role.ADMIN);

        var calendar = Calendar.getInstance();
        var year = calendar.get(Calendar.YEAR) - MIN_YEARS_OLD_MINUS;
        var endDateLimit = year + "-12-31";

        model.addAttribute("startDateLimit", START_DATE_LIMIT);
        model.addAttribute("endDateLimit", endDateLimit);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("user", userDTO);
        return "settings";
    }

    @DeleteMapping
    @Operation(summary = "Видалення акаунта", description = "Видаляє акаунт")
    public ResponseEntity deleteAccount(@AuthenticationPrincipal UserEntity userEntity){
        userService.deleteAccountByNickname(userEntity.getNickname());
        log.info(userEntity.getNickname() + " account was deleted by himself.");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN, USER')")
    @Operation(summary = "Оновлення профілю", description = "Оновлює налаштування користувача")
    public ResponseEntity updateUserInfo(@RequestBody UserForUpdating user, @AuthenticationPrincipal UserEntity userEntity){
        try {
            userService.updateUserInformationFromSettings(user, userEntity.getNickname());
        }catch (EmptyUserDataException e){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
