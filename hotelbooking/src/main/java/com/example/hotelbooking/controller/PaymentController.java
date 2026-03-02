package com.example.hotelbooking.controller;

import com.example.hotelbooking.dto.CreateOrderRequest;
import com.example.hotelbooking.dto.PaymentVerificationRequest;
import com.example.hotelbooking.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create-order")
    public String createOrder(@RequestBody CreateOrderRequest request) {
        return paymentService.createOrder(request);
    }

    @PostMapping("/verify")
    public String verifyPayment(@RequestBody PaymentVerificationRequest request) {
        return paymentService.verifyPayment(request);
    }
}
