package com.example.mipt.repositories;

import com.example.mipt.models.MessageModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessagesRepository extends JpaRepository<MessageModel, Long> {
    List<MessageModel> findByChatId(Long chatId);
}
