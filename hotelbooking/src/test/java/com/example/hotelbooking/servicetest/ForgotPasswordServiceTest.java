package com.example.hotelbooking.servicetest;

import com.example.hotelbooking.entity.PasswordResetOtp;
import com.example.hotelbooking.entity.User;
import com.example.hotelbooking.repository.PasswordResetOtpRepository;
import com.example.hotelbooking.repository.UserRepository;
import com.example.hotelbooking.service.ForgotPasswordService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ForgotPasswordServiceTest {

    @Mock
    private PasswordResetOtpRepository otpRepo;

    @Mock
    private UserRepository userRepo;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ForgotPasswordService service;

    // ✅ TEST SEND OTP
    @Test
    void testSendOtp() {

        service.sendOtp("test@gmail.com");

        verify(otpRepo).deleteByEmail("test@gmail.com");
        verify(otpRepo).save(any(PasswordResetOtp.class));

        // IMPORTANT FIX HERE 👇
        verify(mailSender)
            .send(any(org.springframework.mail.SimpleMailMessage.class));
    }

    // ✅ TEST VERIFY OTP SUCCESS
    @Test
    void testVerifyOtp_Success() {

        PasswordResetOtp otp = new PasswordResetOtp(
                1L,
                "test@gmail.com",
                "123456",
                LocalDateTime.now().plusMinutes(5)
        );

        when(otpRepo.findByEmailAndOtp("test@gmail.com","123456"))
                .thenReturn(Optional.of(otp));

        assertDoesNotThrow(() ->
                service.verifyOtp("test@gmail.com","123456")
        );
    }

    // ✅ TEST RESET PASSWORD
    @Test
    void testResetPassword() {

        User user = new User();
        user.setEmail("test@gmail.com");

        when(userRepo.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.encode("newpass"))
                .thenReturn("encodedPassword");

        service.resetPassword("test@gmail.com","newpass");

        verify(userRepo).save(user);
        verify(otpRepo).deleteByEmail("test@gmail.com");
    }
}