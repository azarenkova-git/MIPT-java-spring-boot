package com.example.mipt.services;

import com.example.mipt.dto.MessageDto;
import com.example.mipt.models.ChatModel;
import com.example.mipt.models.MessageModel;
import com.example.mipt.models.UserModel;
import com.example.mipt.repositories.ChatRepository;
import com.example.mipt.repositories.MessagesRepository;
import com.example.mipt.repositories.UserRepository;
import com.example.mipt.utils.AdminData;
import com.example.mipt.utils.NotificationsChatData;
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
    final private ChatRepository chatRepository;
    final private SseService sseService;

    @Autowired
    public MessageService(
            UserRepository userRepository,
            MessagesRepository messagesRepository,
            ChatRepository chatRepository,
            SseService sseService
    ) {
        this.userRepository = userRepository;
        this.messagesRepository = messagesRepository;
        this.chatRepository = chatRepository;
        this.sseService = sseService;
    }

    public List<MessageModel> findByChatId(Long chatId) {
        return messagesRepository.findByChatId(chatId);
    }

    public void create(String username, Long chatId, MessageDto messageDto) {
        UserModel user = userRepository.findByUsername(username).orElseThrow();
        ChatModel chat = chatRepository.findById(chatId).orElseThrow();
        createInternal(user, chat, messageDto);
    }

    private void create(String username, String chatName, MessageDto messageDto) {
        UserModel user = userRepository.findByUsername(username).orElseThrow();
        ChatModel chat = chatRepository.findByName(chatName).orElseThrow();
        createInternal(user, chat, messageDto);
    }

    private void createInternal(UserModel user, ChatModel chat, MessageDto messageDto) {
        MessageModel message = new MessageModel();
        message.setText(messageDto.getText());
        message.setUser(user);
        message.setChat(chat);
        message.setDate(new Date());
        messagesRepository.save(message);
        String eventName = "chat-" + chat.getId() + "/messageCreated";
        System.out.println("Sending event: " + eventName);
        SseEmitter.SseEventBuilder eventBuilder = SseEmitter.event().name(eventName).data(message.getText());
        sseService.broadcast(eventBuilder);
    }

    /**
     * Создаёт сообщение в чате уведомлений каждую минуту
     */
    @Scheduled(fixedRate = 60000, initialDelay = 60000)
    private void createNewMessageEveryMinute() {
        MessageDto messageDto = new MessageDto();
        messageDto.setText("One minute passed. I am very creative btw.");
        create(AdminData.USERNAME, NotificationsChatData.NAME, messageDto);
    }
}
