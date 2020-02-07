package App.controller;

import App.domain.UserDTO;
import App.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class TemplateController {

    @Autowired
    UserService userService;

    @GetMapping("login")
    public String getLoginView(){
        return "login";
    }

    @GetMapping("confirm")
    public String activateAccount(@RequestParam String token){
        UserDTO userDTO = userService.findByConfirmationToken(token);
        if (userDTO != null) {
            userDTO.setActivated(true);
            userDTO.setConfirmationToken(null);
            userService.updateUser(userDTO);
        }
        return "redirect:/login";
    }

    @GetMapping
    public String getMainPageView(){
        return "main";
    }

    @GetMapping("profile")
    public String getProfileView(){
        return "profile";
    }
}
