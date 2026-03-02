package com.example.hotelbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.hotelbooking.entity.Payment;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);

}


