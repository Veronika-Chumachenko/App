package ua.opnu.practice1_template.logController;

import org.springframework.web.bind.annotation.GetMapping;

public class UserController {
    @GetMapping("/user")
    public String user(){
        return "user";
    }
}
