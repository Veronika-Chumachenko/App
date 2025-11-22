package ua.opnu.practice1_template.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ua.opnu.practice1_template.security.User;
import ua.opnu.practice1_template.security.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

@Controller
public class OAuth2Controller {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public OAuth2Controller(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/oauth2/success")
    public String oauth2Success(@AuthenticationPrincipal OAuth2User oauth2User, Model model) {
        if (oauth2User != null) {
            Map<String, Object> attributes = oauth2User.getAttributes();

            //Отримання даних
            String email = getEmailFromAttributes(attributes);
            String name = getNameFromAttributes(attributes);
            String role = determineUserRole(email, name);

            //Створення користувача
            User user = createOrUpdateUser(email, name, role);

            //Додавання даних в модель
            model.addAttribute("username", name);
            model.addAttribute("email", email);
            model.addAttribute("role", role);
            model.addAttribute("oauthProvider", "GitHub");

            System.out.println("=== OAUTH2 USER CREATED ===");
            System.out.println("Name: " + name);
            System.out.println("Email: " + email);
            System.out.println("Role: " + role);
        }

        return "oauth2-success";
    }

    private User createOrUpdateUser(String email, String name, String role) {
        return userRepository.findByUsername(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setUsername(email);
                    newUser.setPassword(passwordEncoder.encode("oauth2-" + System.currentTimeMillis()));
                    newUser.setRole(role);
                    newUser.setEmail(email);
                    newUser.setFullName(name);
                    newUser.setOauthProvider("github");
                    return userRepository.save(newUser);
                });
    }

    private String getEmailFromAttributes(Map<String, Object> attributes) {
        String email = (String) attributes.get("email");
        if (email == null) {
            Integer id = (Integer) attributes.get("id");
            email = "github-" + id + "@users.noreply.github.com";
        }
        return email;
    }

    private String getNameFromAttributes(Map<String, Object> attributes) {
        String name = (String) attributes.get("name");
        if (name == null) {
            name = (String) attributes.get("login");
        }
        if (name == null) {
            name = "GitHub User";
        }
        return name;
    }

    private String determineUserRole(String email, String username) {
        if (email.contains("admin") || (username != null && username.contains("admin"))) {
            return "ROLE_ADMIN";
        } else if (email.contains("instructor") || (username != null && username.contains("instructor"))) {
            return "ROLE_INSTRUCTOR";
        } else {
            return "ROLE_STUDENT";
        }
    }
}