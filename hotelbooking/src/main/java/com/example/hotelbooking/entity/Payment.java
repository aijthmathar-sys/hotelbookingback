package com.example.hotelbooking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;
    private String status;

    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;

    private LocalDateTime paymentDate;

    @OneToOne
    private Booking booking;

    
}