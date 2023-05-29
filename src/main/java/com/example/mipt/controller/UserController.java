package com.example.mipt.controller;

import com.example.mipt.dto.UserDto;
import com.example.mipt.models.UserModel;
import com.example.mipt.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/api/user")
@EnableWebMvc
public class UserController {
    final private UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/create")
    public RedirectView createUser(@ModelAttribute UserDto userDto) {
        UserModel user = new UserModel();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        userRepository.save(user);
        return new RedirectView("/users");
    }

    @GetMapping("/find/{id}")
    public UserModel find(@PathVariable("id") String id) {
        return userRepository.findById(id).orElseThrow();
    }

    @GetMapping("/find")
    public Iterable<UserModel> findAll() {
        return userRepository.findAll();
    }
}
