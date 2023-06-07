package com.example.mipt.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "chat")
public class ChatModel extends AbstractModel {
    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "chat", fetch = FetchType.LAZY)
    private List<MessageModel> messages;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<MessageModel> getMessages() {
        return messages;
    }
}
