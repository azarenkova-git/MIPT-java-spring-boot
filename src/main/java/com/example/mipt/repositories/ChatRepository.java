package com.example.mipt.repositories;

import com.example.mipt.models.ChatModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<ChatModel, Long> {
    Optional<ChatModel> findByName(String name);
}
