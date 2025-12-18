package com.example.myChat.job;

import com.example.myChat.Model.User;
import com.example.myChat.Repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class DeleteExpiredUsersJob {

    private final UserRepository userRepository;

    public DeleteExpiredUsersJob(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Scheduled(fixedRate = 2 * 60 * 1000)
    public void deleteExpiredUsers() {
        Instant now = Instant.now();
        List<User> expiredUsers = userRepository.findAllByEnabledFalseAndEmailVerificationExpiresInBefore(now);

        if (!expiredUsers.isEmpty()) {
            userRepository.deleteAll(expiredUsers);
            System.out.println("[JOB] DELETED EXPIRED USERS");
        } else {
            System.out.println("[JOB] NO EXPIRED ACCOUNTS FOUND");
        }
    }
}

