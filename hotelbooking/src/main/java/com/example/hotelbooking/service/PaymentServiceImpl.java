package com.example.hotelbooking.service;

import com.example.hotelbooking.dto.CreateOrderRequest;
import com.example.hotelbooking.dto.PaymentVerificationRequest;
import com.example.hotelbooking.entity.Booking;
import com.example.hotelbooking.entity.BookingStatus;
import com.example.hotelbooking.entity.Room;
import com.example.hotelbooking.entity.User;
import com.example.hotelbooking.repository.BookingRepository;
import com.example.hotelbooking.repository.RoomRepository;
import com.example.hotelbooking.repository.UserRepository;
import com.example.hotelbooking.service.PaymentService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import jakarta.transaction.Transactional;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private UserRepository userRepository;

    @Value("${razorpay.key.id}")
private String keyId;

@Value("${razorpay.key.secret}")
private String keySecret;
    public String createOrder(CreateOrderRequest request) {

    try {
          System.out.println(keyId);
          System.out.println(keySecret);
          System.out.println(request.getAmount());
        RazorpayClient client = new RazorpayClient(keyId, keySecret);

        JSONObject options = new JSONObject();
        options.put("amount", Math.round(request.getAmount() * 100));
        options.put("currency", "INR");
        options.put("receipt", "order_" + System.currentTimeMillis());

        Order order = client.orders.create(options);
        System.out.println("order="+order.get("id"));

        return order.get("id").toString();

    } catch (Exception e) {
        System.out.println("RAZORPAY ERROR:"+ e.getMessage());
        e.printStackTrace();
        throw new RuntimeException("Error creating Razorpay order", e);
    }
}
    @Override
    @Transactional
    public String verifyPayment(PaymentVerificationRequest request) {

        try {

            String data = request.getRazorpayOrderId() + "|" + request.getRazorpayPaymentId();
            String generatedSignature = hmacSHA256(data, keySecret);
            System.out.println(request.getRazorpayOrderId());
                System.out.println(request.getRazorpayPaymentId());
                System.out.println(request.getRazorpaySignature());

            if (!generatedSignature.equals(request.getRazorpaySignature())) {
                throw new RuntimeException("Payment verification failed");
            }
               
            // ✅ Payment success → create booking
            Booking booking = new Booking();

            booking.setCheckInDate(request.getCheckInDate());
            booking.setCheckOutDate(request.getCheckOutDate());
            booking.setGuests(request.getGuests());
            booking.setTotalPrice(request.getTotalPrice());
            booking.setStatus(BookingStatus.CONFIRMED);
            Room room = roomRepository.findById(request.getRoomId())
        .orElseThrow(() -> new RuntimeException("Room not found"));

             booking.setRoom(room);
             String email = SecurityContextHolder.getContext()
        .getAuthentication()
        .getName();

User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("User not found"));

booking.setUser(user);

            bookingRepository.save(booking);

            return "Booking Confirmed";

        } catch (Exception e) {
            throw new RuntimeException("Verification failed");
        }
    }

 private String hmacSHA256(String data, String key) {
    try {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA256");
        mac.init(secretKey);
        byte[] rawHmac = mac.doFinal(data.getBytes());

        StringBuilder hex = new StringBuilder(2 * rawHmac.length);
        for (byte b : rawHmac) {
            String hexByte = Integer.toHexString(0xff & b);
            if (hexByte.length() == 1) hex.append('0');
            hex.append(hexByte);
        }
        return hex.toString();

    } catch (Exception e) {
        throw new RuntimeException("Failed to generate HMAC", e);
    }

}}