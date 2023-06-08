package com.example.mipt.services;

import com.example.mipt.dto.ChatDto;
import com.example.mipt.models.ChatModel;
import com.example.mipt.repositories.ChatRepository;
import com.example.mipt.utils.NotificationsChatData;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Service
public class ChatService {
    final private ChatRepository chatRepository;
    final private SseService sseService;

    @Autowired
    public ChatService(ChatRepository chatRepository, SseService sseService) {
        this.chatRepository = chatRepository;
        this.sseService = sseService;
    }

    public List<ChatModel> findAll() {
        return chatRepository.findAll();
    }

    public ChatModel create(ChatDto chatDto) {
        ChatModel chat = new ChatModel();
        chat.setName(chatDto.getName());
        chatRepository.save(chat);
        String eventName = "chatCreated";
        SseEmitter.SseEventBuilder eventBuilder = SseEmitter.event().name(eventName).data(chat.getName());
        sseService.broadcast(eventBuilder);
        return chat;
    }

    /**
     * Создаёт чат для уведомлений, если его ещё нет.
     */
    @PostConstruct
    private void initNotificationsChat() {
        if (chatRepository.existsByName(NotificationsChatData.NAME)) {
            return;
        }

        ChatDto chatDto = new ChatDto();
        chatDto.setName(NotificationsChatData.NAME);
        create(chatDto);
    }
}
