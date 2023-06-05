package com.example.mipt.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

@Entity
@Table(name = "app_user")
public class UserModel extends AbstractModel {
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @OneToMany()
    private Set<MessageModel> messages;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<MessageModel> getMessages() {
        return messages;
    }

    public UserDetails toUserDetails() {
        return User.builder()
                .username(getUsername())
                .password(getPassword())
                .roles("USER")
                .build();
    }
}
