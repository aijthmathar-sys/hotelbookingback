package com.example.hotelbooking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hotelbooking.entity.User;
import java.util.List;


public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User>  findByEmail(String email);
    
    boolean existsByEmail(String email);
    
}
