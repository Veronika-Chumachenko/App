package ua.opnu.practice1_template.logController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.opnu.practice1_template.security.User;
import ua.opnu.practice1_template.security.UserRepository;

@Service
@Transactional
public class RegisterService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerSubmit(User user, String role) {
        System.out.println("=== ДЕТАЛЬНА РЕЄСТРАЦІЯ ===");
        System.out.println("Username: " + user.getUsername());
        System.out.println("Original Password: " + user.getPassword());
        System.out.println("PasswordEncoder: " + passwordEncoder.getClass().getSimpleName());

        //Перевірка існування користувача
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Користувач вже існує!");
        }

        //Кодування паролю
        String rawPassword = user.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encodedPassword);

        System.out.println("Raw Password: " + rawPassword);
        System.out.println("Encoded Password: " + encodedPassword);
        System.out.println("Password matches check: " + passwordEncoder.matches(rawPassword, encodedPassword));

        //Встановлення ролі
        switch(role.toUpperCase()) {
            case "ADMIN":
                user.setRole("ROLE_ADMIN");
                break;
            case "INSTRUCTOR":
                user.setRole("ROLE_INSTRUCTOR");
                break;
            case "STUDENT":
            default:
                user.setRole("ROLE_STUDENT");
        }

        //Збереження користувача
        User savedUser = userRepository.save(user);
        System.out.println("Користувач збережений:");
        System.out.println("ID: " + savedUser.getId());
        System.out.println("Username: " + savedUser.getUsername());
        System.out.println("Role: " + savedUser.getRole());
        System.out.println("Password in DB: " + savedUser.getPassword());


        //Перевірка після збереження
        User verifiedUser = userRepository.findByUsername(savedUser.getUsername()).get();
        System.out.println("Перевірка після збереження:");
        System.out.println("Password matches: " + passwordEncoder.matches(rawPassword, verifiedUser.getPassword()));
        System.out.println("=== РЕЄСТРАЦІЯ ЗАВЕРШЕНА ===");
    }
}
