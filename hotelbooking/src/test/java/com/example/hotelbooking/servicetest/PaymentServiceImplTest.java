package com.example.hotelbooking.servicetest;

import com.example.hotelbooking.dto.PaymentVerificationRequest;
import com.example.hotelbooking.entity.*;
import com.example.hotelbooking.repository.BookingRepository;
import com.example.hotelbooking.repository.RoomRepository;
import com.example.hotelbooking.repository.UserRepository;
import com.example.hotelbooking.service.PaymentServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class PaymentServiceImplTest {

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private PaymentVerificationRequest request;

    @BeforeEach
    void setup() {
        request = new PaymentVerificationRequest();
        request.setRazorpayOrderId("order123");
        request.setRazorpayPaymentId("payment123");
        request.setCheckInDate(java.time.LocalDate.now());
        request.setCheckOutDate(java.time.LocalDate.now().plusDays(1));
        request.setGuests(2);
        request.setTotalPrice(5000.0);
        request.setRoomId(1L);
    }

    @Test
    void verifyPayment_success() throws Exception {

        // Arrange
        String keySecret = "test_secret";
        org.springframework.test.util.ReflectionTestUtils
                .setField(paymentService, "keySecret", keySecret);

        // Generate valid signature
        String data = request.getRazorpayOrderId() + "|" + request.getRazorpayPaymentId();
        String validSignature = org.springframework.test.util.ReflectionTestUtils
                .invokeMethod(paymentService, "hmacSHA256", data, keySecret);

        request.setRazorpaySignature(validSignature);

        Room room = new Room();
        room.setId(1L);

        User user = new User();
        user.setEmail("test@example.com");

        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");

        SecurityContextHolder.setContext(securityContext);

        // Act
        String result = paymentService.verifyPayment(request);

        // Assert
        assertEquals("Booking Confirmed", result);
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void verifyPayment_signatureMismatch() {

        request.setRazorpaySignature("invalid_signature");

        assertThrows(RuntimeException.class, () ->
                paymentService.verifyPayment(request));
    }

    @Test
    void verifyPayment_roomNotFound() throws Exception {

        String keySecret = "test_secret";
        org.springframework.test.util.ReflectionTestUtils
                .setField(paymentService, "keySecret", keySecret);

        String data = request.getRazorpayOrderId() + "|" + request.getRazorpayPaymentId();
        String validSignature = org.springframework.test.util.ReflectionTestUtils
                .invokeMethod(paymentService, "hmacSHA256", data, keySecret);

        request.setRazorpaySignature(validSignature);

        when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                paymentService.verifyPayment(request));
    }
}