package ua.opnu.practice1_template.logController;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ua.opnu.practice1_template.security.JwtTokenProvider;
import ua.opnu.practice1_template.security.User;

@Controller
public class AuthController {

    @Autowired
    private RegisterService registerService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(@ModelAttribute("user") User user,
                                 @RequestParam String role,
                                 Model model) {
        try {
        registerService.registerSubmit(user, role);
        return "redirect:/login?success";
    } catch (Exception e) {
        model.addAttribute("error", "Помилка реєстрації: " + e.getMessage());
        return "register";
        }
    }

    @GetMapping("/login")
    public String loginForm(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            @RequestParam(value = "success", required = false) String success,
                            Model model) {
        if (error != null) {
            model.addAttribute("error", "Невірне ім'я користувача або пароль!");
        }
        if (logout != null) {
            model.addAttribute("message", "Ви успішно вийшли з системи!");
        }
        if (success != null) {
            model.addAttribute("message", "Реєстрація успішна! Можете увійти.");
        }
        return "login";
    }

    @GetMapping("/success")
    public String success(Authentication auth) {
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/admin";
        }
        return "redirect:/user";
    }


    //JWT Authentication API
    @RestController
    @RequestMapping("/api/auth")
    public static class JwtAuthController {

        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        private JwtTokenProvider tokenProvider;

        @PostMapping("/login")
        public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
            try {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getUsername(),
                                loginRequest.getPassword()
                        )
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
                String jwt = tokenProvider.generateToken(authentication);

                return ResponseEntity.ok(new JwtResponse(jwt, "Bearer"));
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Невірний логін або пароль"));
            }
        }
    }

    //DTO класи для JWT
    @Setter
    @Getter
    public static class LoginRequest {
        private String username;
        private String password;

    }

    @Getter
    public static class JwtResponse {
        private String token;
        private String type;

        public JwtResponse(String token, String type) {
            this.token = token;
            this.type = type;
        }

    }

    @Getter
    public static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

    }
}