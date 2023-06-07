package com.example.mipt.repositories;

import com.example.mipt.models.UserModel;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByUsername(String username);
    Boolean existsByUsername(String username);
}
