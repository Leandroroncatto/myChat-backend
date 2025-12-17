package com.example.myChat.Service;

public interface EmailSender {
    void sendVerificationEmail(String email, String token);

    void sendForgotPasswordEmail(String email, String token);
}
