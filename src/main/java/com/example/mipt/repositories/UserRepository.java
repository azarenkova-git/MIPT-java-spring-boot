package com.example.mipt.repositories;

import com.example.mipt.models.UserModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel, String> {
    UserModel findByUsername(String username);
}
