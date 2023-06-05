package com.example.mipt.services;

import com.example.mipt.dto.UserDto;
import com.example.mipt.models.UserModel;
import com.example.mipt.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserModel> findAll() {
        return userRepository.findAll();
    }

    public void create(UserDto userDto) {
        UserModel user = new UserModel();
        user.setUsername(userDto.getUsername());
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    @PostConstruct
    private void initAdmin() {
        UserModel existingAdmin = userRepository.findByUsername("admin");

        if (existingAdmin != null) {
            return;
        }

        UserDto dto = new UserDto();
        dto.setUsername("admin");
        dto.setPassword("password");
        create(dto);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            UserModel userModel = userRepository.findByUsername(username);

            if (userModel == null) {
                throw new UsernameNotFoundException(username);
            }

            return userModel.toUserDetails();
        };
    }
}
