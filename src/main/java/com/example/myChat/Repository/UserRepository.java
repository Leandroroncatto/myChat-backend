package com.example.myChat.Repository;

import com.example.myChat.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsernameOrEmail(String name, String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
