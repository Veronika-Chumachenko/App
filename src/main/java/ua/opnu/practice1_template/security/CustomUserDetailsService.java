package ua.opnu.practice1_template.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("=== СПРОБА ЗАВАНТАЖИТИ КОРИСТУВАЧА ===");
        System.out.println("Шукаємо користувача: " + username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    System.out.println("Користувача не знайдено: " + username);
                    return new UsernameNotFoundException("User " + username + " not found!");
                });

        System.out.println("Користувач знайдений: " + user.getUsername() + ", роль: " + user.getRole());
        System.out.println("=== КОРИСТУВАЧ ЗАВАНТАЖЕНИЙ ===");

        return user;
    }
}