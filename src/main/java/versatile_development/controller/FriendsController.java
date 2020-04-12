package versatile_development.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/friends")
public class FriendsController {

    @GetMapping
    public String getFriendsTemplate(){
        return "friends";
    }
}
