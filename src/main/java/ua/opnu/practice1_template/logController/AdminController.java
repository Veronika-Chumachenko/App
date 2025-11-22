package ua.opnu.practice1_template.logController;

import org.springframework.web.bind.annotation.GetMapping;

public class AdminController {
    @GetMapping("/admin")
    public String admin(){
        return "admin";
    }
}
