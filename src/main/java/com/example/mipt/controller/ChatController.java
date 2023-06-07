package com.example.mipt.controller;

import com.example.mipt.dto.ChatDto;
import com.example.mipt.models.ChatModel;
import com.example.mipt.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
public class ChatController {
    final private ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/chats")
    public String messages(Model model) {
        List<ChatModel> chats = chatService.findAll();
        model.addAttribute("chats", chats);
        ChatDto chatDto = new ChatDto();
        model.addAttribute("chatDto", chatDto);
        return "chats";
    }

    @GetMapping("/createChat")
    public String createChat(Model model) {
        ChatDto chatDto = new ChatDto();
        model.addAttribute("chatDto", chatDto);
        return "createChat";
    }

    @PostMapping("/api/chat/create")
    public RedirectView createMessage(@ModelAttribute ChatDto chatDto) {
        ChatModel chat = chatService.create(chatDto);
        return new RedirectView("/messages/" + chat.getId());
    }
}
