package com.example.hotelbooking.controller;

import com.example.hotelbooking.dto.CreateOrderRequest;
import com.example.hotelbooking.dto.PaymentVerificationRequest;
import com.example.hotelbooking.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateOrder() throws Exception {

        CreateOrderRequest request = new CreateOrderRequest();
        request.setAmount(5000);

        when(paymentService.createOrder(Mockito.any(CreateOrderRequest.class)))
                .thenReturn("ORDER_CREATED");

        mockMvc.perform(post("/api/payment/create-order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("ORDER_CREATED"));
    }

    @Test
    void testVerifyPayment() throws Exception {

        PaymentVerificationRequest request = new PaymentVerificationRequest();
        request.setRazorpayOrderId("order_123");
        request.setRazorpayPaymentId("pay_123");
        request.setRazorpaySignature("sig_123");

        when(paymentService.verifyPayment(Mockito.any(PaymentVerificationRequest.class)))
                .thenReturn("PAYMENT_SUCCESS");

        mockMvc.perform(post("/api/payment/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("PAYMENT_SUCCESS"));
    }
}