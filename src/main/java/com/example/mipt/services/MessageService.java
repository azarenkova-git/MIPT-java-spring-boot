package com.example.mipt.services;


import com.example.mipt.dto.MessageDto;
import com.example.mipt.models.MessageModel;
import com.example.mipt.models.UserModel;
import com.example.mipt.repositories.MessagesRepository;
import com.example.mipt.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Date;
import java.util.List;

@Service
public class MessageService {
    final private UserRepository userRepository;
    final private MessagesRepository messagesRepository;
    final private SseService sseService;

    @Autowired
    public MessageService(
            UserRepository userRepository,
            MessagesRepository messagesRepository,
            SseService sseService
    ) {
        this.userRepository = userRepository;
        this.messagesRepository = messagesRepository;
        this.sseService = sseService;
    }

    public List<MessageModel> findAll() {
        return messagesRepository.findAll();
    }

    public void createMessage(String username, MessageDto messageDto) {
        UserModel user = userRepository.findByUsername(username);
        MessageModel message = new MessageModel();
        message.setText(messageDto.getText());
        message.setUser(user);
        message.setDate(new Date());
        messagesRepository.save(message);
        sseService.broadcast(SseEmitter.event().name("message").data(message));
    }

    @Scheduled(fixedRate = 60000)
    private void createNewMessageEveryMinute() {
        System.out.println("Creating new message");
        MessageDto messageDto = new MessageDto();
        messageDto.setText("New message " + new Date());
        createMessage("admin", messageDto);
    }
}
