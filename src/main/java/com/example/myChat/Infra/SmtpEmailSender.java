package com.example.myChat.Infra;

import com.example.myChat.Service.EmailSender;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.SecureRandom;

@Service
public class SmtpEmailSender implements EmailSender {

    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String from;

    public SmtpEmailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendVerificationEmail(String email, String verificationToken) {
        String subject = "Email verification";
        String path = "/verify";
        String message = "Click the button below to verify your Email address";
        sendEmail(email, verificationToken, subject, path, message);
    }

    public void sendForgotPasswordEmail(String email, String resetToken) {
        String subject = "Password reset request";
        String path = "/reset";
        String message = "Click the button below to reset your password";
        sendEmail(email, resetToken, subject, path, message);
    }

    public void sendTwoFactorAuthenticationEmail(String email, String twoFactorToken) {
        String subject = "Enable 2FA";
        String path = "";
        String message = "Click the button below to enable two factor authentication";
        sendEmail(email, twoFactorToken, subject, path, message);
    }

    private void sendEmail(String email, String token, String subject, String path, String message) {
        try {
            URI myChatUri = URI.create("https://mychat-1wyo.onrender.com");
            String actionUrl = ServletUriComponentsBuilder.fromUri(myChatUri)
                    .path(path)
                    .queryParam("token", token).toUriString();
            String content = """
                    <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px; border-radius: 8px; background-color: #f9f9f9; text-align: center;">
                      <h2 style="color: #333;">%s</h2>
                    
                      <p style="font-size: 16px; color: #555;">
                        %s.
                      </p>
                    
                      <a href="%s"
                         style="
                           display: inline-block;
                           margin: 20px 0;
                           padding: 12px 24px;
                           font-size: 16px;
                           color: #ffffff;
                           background-color: #3B82F6;
                           text-decoration: none;
                           border-radius: 6px;
                           font-weight: bold;
                         ">
                        Verify Email
                      </a>
                    
                      <p style="font-size: 14px; color: #777;">
                        If the button doesn't work, copy and paste this link into your browser:
                      </p>
                    
                      <p style="font-size: 14px; color: #007bff; word-break: break-all;">
                        %s
                      </p>
                    
                      <p style="font-size: 12px; color: #aaa;">
                        This is an automated message. Please do not reply to this email.
                      </p>
                    </div>
                    """.formatted(subject, message, actionUrl, actionUrl);

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setFrom(from);
            helper.setText(content, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
