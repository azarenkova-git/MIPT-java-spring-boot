package com.example.mipt.controller;

import com.example.mipt.dto.UserDto;
import com.example.mipt.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PagesController {
    final private UserRepository userRepository;

    @Autowired
    public PagesController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/createUser")
    public String createUserForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "createUser";
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "users";
    }
}
