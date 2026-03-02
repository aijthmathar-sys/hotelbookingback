package com.example.hotelbooking.controller;

import com.example.hotelbooking.dto.RegisterRequest;
import com.example.hotelbooking.entity.Role;
import com.example.hotelbooking.entity.User;
import com.example.hotelbooking.security.JwtAuthenticationFilter;
import com.example.hotelbooking.security.JwtUtil;
import com.example.hotelbooking.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(UserControllerTest.TestSecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private JwtUtil jwtUtil;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {

        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setName("Mathar");
        request.setEmail("mathar@gmail.com");
        request.setPassword("123456");

        User savedUser = User.builder()
                .id(1L)
                .name("Mathar")
                .email("mathar@gmail.com")
                .password("encodedPassword")
                .role(Role.USER)
                .build();

        Mockito.when(userService.register(Mockito.any(User.class)))
                .thenReturn(savedUser);

        // Act & Assert
        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Mathar"))
                .andExpect(jsonPath("$.email").value("mathar@gmail.com"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    // 🔐 Fake Security Bean to avoid context failure
    @TestConfiguration
    static class TestSecurityConfig {

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }
}