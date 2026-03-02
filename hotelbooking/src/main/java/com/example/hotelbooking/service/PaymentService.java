package com.example.hotelbooking.service;

import com.example.hotelbooking.dto.CreateOrderRequest;
import com.example.hotelbooking.dto.PaymentVerificationRequest;

public interface PaymentService {

    String createOrder(CreateOrderRequest request);

    String verifyPayment(PaymentVerificationRequest request);
}