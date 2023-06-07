package com.example.mipt.controller;

import com.example.mipt.dto.MessageDto;
import com.example.mipt.models.MessageModel;
import com.example.mipt.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
public class MessageController {
    final private MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/messages/{chatId}")
    public String messages(Model model, @PathVariable Long chatId) {
        List<MessageModel> messages = messageService.findByChatId(chatId);
        model.addAttribute("messages", messages);
        MessageDto messageDto = new MessageDto();
        model.addAttribute("messageDto", messageDto);
        model.addAttribute("chatId", chatId);
        return "messages";
    }

    @PostMapping("/api/message/create/{chatId}")
    public RedirectView createMessage(
            @ModelAttribute MessageDto messageDto,
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long chatId
    ) {
        messageService.create(userDetails.getUsername(), chatId, messageDto);
        return new RedirectView("/messages/" + chatId);
    }
}
