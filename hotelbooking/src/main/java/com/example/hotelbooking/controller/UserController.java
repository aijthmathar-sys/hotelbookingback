package com.example.hotelbooking.controller;

import com.example.hotelbooking.dto.RegisterRequest;
import com.example.hotelbooking.dto.UserResponse;
import com.example.hotelbooking.entity.User;
import com.example.hotelbooking.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

  @PostMapping("/register")
public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest request) {

    User user = User.builder()
            .name(request.getName())
            .email(request.getEmail())
            .password(request.getPassword())
            .build();

    User savedUser = userService.register(user);

    UserResponse response = UserResponse.builder()
            .id(savedUser.getId())
            .name(savedUser.getName())
            .email(savedUser.getEmail())
            .role(savedUser.getRole().name())
            .build();

    return ResponseEntity.ok(response);

}
}