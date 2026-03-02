package com.example.hotelbooking.controller;

import com.example.hotelbooking.service.ForgotPasswordService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ForgotPasswordControllerTest {

    @Mock
    ForgotPasswordService service;

    @InjectMocks
    ForgotPasswordController controller;

    @Test
    void testSendOtp() {

        String res = controller.sendOtp("test@gmail.com");

        verify(service).sendOtp("test@gmail.com");
        assertEquals("OTP Sent", res);
    }

    @Test
    void testVerifyOtp() {

        String res = controller.verifyOtp("test@gmail.com","123456");

        verify(service).verifyOtp("test@gmail.com","123456");
        assertEquals("OTP Verified", res);
    }

    @Test
    void testResetPassword() {

        String res = controller.resetPassword("test@gmail.com","newpass");

        verify(service).resetPassword("test@gmail.com","newpass");
        assertEquals("Password Updated", res);
    }
}
