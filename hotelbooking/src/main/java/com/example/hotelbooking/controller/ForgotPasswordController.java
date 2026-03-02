package com.example.hotelbooking.controller;

import com.example.hotelbooking.service.ForgotPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class ForgotPasswordController {

    private final ForgotPasswordService service;

    @PostMapping("/forgot-password")
    public String sendOtp(@RequestParam String email) {
        service.sendOtp(email);
        return "OTP Sent";
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String email,
                            @RequestParam String otp) {
        service.verifyOtp(email, otp);
        return "OTP Verified";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String email,
                                @RequestParam String newPassword) {
        service.resetPassword(email, newPassword);
        return "Password Updated";
    }
}