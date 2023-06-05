package com.example.mipt.controller;

import com.example.mipt.dto.UserDto;
import com.example.mipt.models.UserModel;
import com.example.mipt.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
public class UserController {
    final private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/createUser")
    public String createUserForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "/createUser";
    }

    @PostMapping("/api/user/create")
    public RedirectView createUser(@ModelAttribute UserDto userDto) {
        userService.create(userDto);
        return new RedirectView("/users");
    }

    @GetMapping("/users")
    public String users(Model model) {
        List<UserModel> users = userService.findAll();
        model.addAttribute("users", users);
        return "users";
    }
}
