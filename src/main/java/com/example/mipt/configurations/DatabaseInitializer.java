package com.example.mipt.configurations;

import com.example.mipt.models.UserModel;
import com.example.mipt.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void initAdminUser() {
        System.out.println("Initializing admin user");
        UserModel existingAdmin = userRepository.findByUsername("admin");
        if (existingAdmin == null) {
            UserModel admin = new UserModel();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("password"));
            userRepository.save(admin);
        }
    }
}
