package ua.opnu.practice1_template.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import ua.opnu.practice1_template.security.User;
import ua.opnu.practice1_template.security.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomOAuth2UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oauth2User.getAttributes();

        final String email = getEmailFromProvider(attributes, provider);
        final String name = getNameFromAttributes(attributes, provider);

        final String role = determineUserRole(email, name);

        //Створення або оновлення користувача
        User user = userRepository.findByUsername(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setUsername(email);
                    newUser.setPassword(passwordEncoder.encode("oauth2-password-" + System.currentTimeMillis()));
                    newUser.setRole(role);
                    newUser.setEmail(email);
                    newUser.setFullName(name);
                    newUser.setOauthProvider(provider);
                    return userRepository.save(newUser);
                });

        System.out.println("=== REAL OAUTH2 LOGIN ===");
        System.out.println("Provider: " + provider);
        System.out.println("Email: " + email);
        System.out.println("Name: " + name);
        System.out.println("Role: " + role);

        return oauth2User;
    }

    private String getEmailFromProvider(Map<String, Object> attributes, String provider) {
        if ("github".equals(provider)) {
            String email = (String) attributes.get("email");
            if (email == null) {
                Integer id = (Integer) attributes.get("id");
                email = "github-" + id + "@users.noreply.github.com";
            }
            return email;
        } else if ("google".equals(provider)) {
            return (String) attributes.get("email");
        }
        return (String) attributes.get("email");
    }

    private String getNameFromAttributes(Map<String, Object> attributes, String provider) {
        String name = (String) attributes.get("name");
        if (name == null) {
            name = (String) attributes.get("login");
        }
        if (name == null) {
            name = "OAuth2 User";
        }
        return name;
    }

    private String determineUserRole(String email, String username) {
        // Логіка призначення ролей
        if (email.contains("admin") || (username != null && username.contains("admin"))) {
            return "ROLE_ADMIN";
        } else if (email.contains("instructor") || (username != null && username.contains("instructor"))) {
            return "ROLE_INSTRUCTOR";
        } else {
            return "ROLE_STUDENT";
        }
    }
}