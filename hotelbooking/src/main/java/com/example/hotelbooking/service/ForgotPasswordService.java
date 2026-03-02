package com.example.hotelbooking.service;

import com.example.hotelbooking.entity.PasswordResetOtp;
import com.example.hotelbooking.entity.User;
import com.example.hotelbooking.repository.PasswordResetOtpRepository;
import com.example.hotelbooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ForgotPasswordService {

    private final PasswordResetOtpRepository otpRepo;
    private final UserRepository userRepo;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    public void sendOtp(String email) {

        String otp = String.valueOf(new Random().nextInt(999999));

        PasswordResetOtp entity = new PasswordResetOtp();
        entity.setEmail(email);
        entity.setOtp(otp);
        entity.setExpiryTime(LocalDateTime.now().plusMinutes(5));

        otpRepo.deleteByEmail(email);
        otpRepo.save(entity);

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("Password Reset OTP");
        msg.setText("Your OTP is: " + otp);

        mailSender.send(msg);
    }

    public void verifyOtp(String email, String otp) {

        PasswordResetOtp record =
            otpRepo.findByEmailAndOtp(email, otp)
            .orElseThrow(() -> new RuntimeException("Invalid OTP"));

        if (record.getExpiryTime().isBefore(LocalDateTime.now()))
            throw new RuntimeException("OTP Expired");
    }

    public void resetPassword(String email, String newPassword) {

        User user = userRepo.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);

        otpRepo.deleteByEmail(email);
    }
}