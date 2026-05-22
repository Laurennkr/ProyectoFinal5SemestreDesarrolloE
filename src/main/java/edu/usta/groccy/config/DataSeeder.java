package edu.usta.groccy.config;

import edu.usta.groccy.entity.User;
import edu.usta.groccy.enums.Role;
import edu.usta.groccy.enums.Status;
import edu.usta.groccy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        createUserIfNotExists(
                "Administrador Groccy",
                "admin@groccy.com",
                "Admin12345",
                Role.ADMIN
        );

        createUserIfNotExists(
                "Vendedor Groccy",
                "vendedor@groccy.com",
                "Vendedor12345",
                Role.SELLER
        );

        createUserIfNotExists(
                "Costurero Groccy",
                "costurero@groccy.com",
                "Costurero12345",
                Role.TAILOR
        );
    }

    private void createUserIfNotExists(String fullName, String email, String rawPassword, Role role) {
        if (!userRepository.existsByEmail(email)) {
            User user = User.builder()
                    .fullName(fullName)
                    .email(email)
                    .password(passwordEncoder.encode(rawPassword))
                    .role(role)
                    .status(Status.ACTIVE)
                    .build();

            userRepository.save(user);
        }
    }
}