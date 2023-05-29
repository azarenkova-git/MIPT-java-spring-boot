package com.example.mipt.controller;

import com.example.mipt.dto.UserDto;
import com.example.mipt.models.UserModel;
import com.example.mipt.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
public class PagesController {
    final private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PagesController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/createUser")
    public String createUserForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "/createUser";
    }

    @PostMapping("/api/user/create")
    public RedirectView createUser(@ModelAttribute UserDto userDto) {
        UserModel user = new UserModel();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user);
        return new RedirectView("/users");
    }

    @GetMapping("/users")
    public String users(Model model) {
        List<UserModel> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
