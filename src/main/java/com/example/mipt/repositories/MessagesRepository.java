package com.example.mipt.repositories;

import com.example.mipt.models.MessageModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessagesRepository extends JpaRepository<MessageModel, String> {
}
