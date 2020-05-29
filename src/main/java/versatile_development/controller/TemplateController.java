package versatile_development.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import versatile_development.domain.dto.UserDTO;
import versatile_development.entity.UserEntity;
import versatile_development.service.UserService;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

@Slf4j
@Controller
@RequestMapping("/")
public class TemplateController {

    UserService userService;

    @Autowired
    TemplateController(@Qualifier(value = "userServiceImpl") UserService userService){
        this.userService = userService;
    }

    @GetMapping("login")
    public String getLoginView(@AuthenticationPrincipal UserEntity userEntity){
        if (userEntity != null)return "redirect:/profile";
        return "login";
    }

    @GetMapping("registration")
    public String getRegistrationView(@AuthenticationPrincipal UserEntity userEntity){
        if (userEntity != null)return "redirect:/profile";
        return "registration";
    }

    @GetMapping("confirm")
    public String activateAccount(@RequestParam String token){
        UserDTO userDTO = userService.findByConfirmationToken(token);
        if (userDTO != null) {
            userDTO.setActivated(true);
            userDTO.setConfirmationToken(null);
            userService.updateUser(userDTO);

            log.info(userDTO.getNickname() + " confirmed registration.");
        }
        return "redirect:/login";
    }

    @GetMapping
    public String getMainPageView(){
        return "main";
    }

    @GetMapping("profile")
    @PreAuthorize("hasAnyAuthority('ADMIN, USER')")
    public String getProfileView(@AuthenticationPrincipal UserEntity user, Model model, Locale locale){
        DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.LONG, locale);
        String currentDate = dateFormatter.format(new Date());

        model.addAttribute("date", currentDate);
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getAllUsers(Model model){
        model.addAttribute("users", userService.findAllUsers());
        return "all_users";
    }

    @GetMapping("/messages")
    public String getMessagePage(){
        return "messages";
    }
}
