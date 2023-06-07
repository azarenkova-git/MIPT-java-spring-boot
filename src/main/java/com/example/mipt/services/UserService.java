package com.example.mipt.services;

import com.example.mipt.dto.UserDto;
import com.example.mipt.models.UserModel;
import com.example.mipt.repositories.UserRepository;
import com.example.mipt.utils.AdminData;
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
        if (userRepository.existsByUsername(AdminData.USERNAME)) {
            return;
        }

        UserDto dto = new UserDto();
        dto.setUsername(AdminData.USERNAME);
        dto.setPassword(AdminData.PASSWORD);
        create(dto);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            UserModel userModel = userRepository
                    .findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException(username));

            return userModel.toUserDetails();
        };
    }
}
