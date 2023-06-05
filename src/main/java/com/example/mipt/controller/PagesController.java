package com.example.mipt.controller;

import com.example.mipt.dto.MessageDto;
import com.example.mipt.dto.UserDto;
import com.example.mipt.models.MessageModel;
import com.example.mipt.models.UserModel;
import com.example.mipt.repositories.MessagesRepository;
import com.example.mipt.repositories.UserRepository;
import com.example.mipt.services.SseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Date;
import java.util.List;

@Controller
public class PagesController {
    final private UserRepository userRepository;
    final private MessagesRepository messagesRepository;
    final private PasswordEncoder passwordEncoder;
    final private SseService sseService;

    @Autowired
    public PagesController(
            UserRepository userRepository,
            MessagesRepository messagesRepository,
            PasswordEncoder passwordEncoder,
            SseService sseService
    ) {
        this.userRepository = userRepository;
        this.messagesRepository = messagesRepository;
        this.passwordEncoder = passwordEncoder;
        this.sseService = sseService;
    }

    @GetMapping(path = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter sse() {
        return sseService.init();
    }

    @GetMapping("/createUser")
    public String createUserForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "/createUser";
    }

    @GetMapping("/messages")
    public String messages(Model model) {
        List<MessageModel> messages = messagesRepository.findAll();
        model.addAttribute("messages", messages);
        MessageDto messageDto = new MessageDto();
        model.addAttribute("message", messageDto);
        return "messages";
    }

    @PostMapping("/api/message/create")
    public RedirectView createMessage(
            @ModelAttribute MessageDto messageDto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UserModel user = userRepository.findByUsername(userDetails.getUsername());
        MessageModel message = new MessageModel();
        message.setText(messageDto.getText());
        message.setUser(user);
        message.setDate(new Date());
        messagesRepository.save(message);
        sseService.broadcast(SseEmitter.event().name("message").data(message));
        return new RedirectView("/messages");
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
