package com.example.hotelbooking.controller;

import com.example.hotelbooking.security.JwtUtil;
import com.example.hotelbooking.dto.AuthResponse;
import com.example.hotelbooking.dto.LoginRequest;
import com.example.hotelbooking.entity.User;
import com.example.hotelbooking.repository.UserRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

   @PostMapping("/login")
public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request,
                                          HttpServletResponse response) {

    authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            )
    );

    User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));

    String token = jwtUtil.generateToken(request.getEmail());

    Cookie cookie = new Cookie("jwt", token);
    cookie.setHttpOnly(true);
    cookie.setSecure(false);
    cookie.setPath("/");
    cookie.setMaxAge(60 * 60);
    

    response.addCookie(cookie);

    
    return ResponseEntity.ok(
            new AuthResponse(user.getRole().name())
    );
}

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {

        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(24*60*60);
        cookie.setAttribute("SameSite","None");

        response.addCookie(cookie);

        return ResponseEntity.ok("Logged out successfully");
    }
    @GetMapping("/me")
public ResponseEntity<?> getCurrentUser(Authentication authentication) {

    if (authentication == null || !authentication.isAuthenticated()) {
        return ResponseEntity.status(401).build();
    }

    String role = authentication.getAuthorities()
            .iterator()
            .next()
            .getAuthority();

    return ResponseEntity.ok(Map.of("role", role));
}
}