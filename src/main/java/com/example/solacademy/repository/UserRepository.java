package com.example.solacademy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.solacademy.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}

