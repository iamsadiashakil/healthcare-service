package com.healthapp.healthcare_service.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPasswordResetEmail(String toEmail, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Password Reset Request Token");
        message.setText("To reset your password, I have shared the api curl below. Please use this token:" + token + "\n\n" +
                "curl -X 'POST' \\\n" +
                "  'http://localhost:8080/api/auth/reset-password' \\\n" +
                "  -H 'accept: */*' \\\n" +
                "  -H 'Content-Type: application/json' \\\n" +
                "  -d '{\n" +
                "  \"token\": \"string\",\n" +
                "  \"newPassword\": \"new password\",\n" +
                "  \"confirmPassword\": \"new password\"\n" +
                "}'");

        mailSender.send(message);
    }
}
