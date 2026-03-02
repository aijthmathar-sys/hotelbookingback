package com.example.hotelbooking.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
            .cors(cors->{})
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth

                
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/hotels/**").permitAll()
                .requestMatchers(
                 "/v3/api-docs/**",
                  "/swagger-ui/**",
                  "/swagger-ui.html"
                ).permitAll()

                
                .requestMatchers(HttpMethod.POST, "/api/hotels/**").hasRole("ADMIN")
                
                .requestMatchers(HttpMethod.GET, "/api/rooms/**").permitAll()
                 .requestMatchers(HttpMethod.POST, "/api/rooms/**").hasRole("ADMIN")
                  .requestMatchers(HttpMethod.PUT, "/api/rooms/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/rooms/**").hasRole("ADMIN")
                    .requestMatchers("/api/bookings/**").hasRole("USER")
                    .requestMatchers("/api/availability/**").permitAll()
                    .requestMatchers("/api/payment/create-order").permitAll()
                    .requestMatchers("/api/payment/verify").hasRole("USER")
                    .requestMatchers("/api/auth/forgot-password",
                 "/api/auth/verify-otp",
                 "/api/auth/reset-password").permitAll()

                .requestMatchers(HttpMethod.PUT, "/api/hotels/**")
                .hasRole("ADMIN")

                .requestMatchers(HttpMethod.DELETE, "/api/hotels/**")
                .hasRole("ADMIN")

                .anyRequest().authenticated()
            )
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );

        http.addFilterBefore(jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
}