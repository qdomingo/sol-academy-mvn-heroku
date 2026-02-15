package com.example.solacademy.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.solacademy.model.User;
import com.example.solacademy.repository.UserRepository;

import java.util.Optional;

@RestController
@CrossOrigin(origins = {"http://solacademy.qdomingo.com", "http://localhost:4200", "http://localhost:4300",
"http://solacademy-aws.qdomingo.com:4200"})
@RequestMapping("/api/auth")
public class AuthResource {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "User registered successfully";
    }

    @PostMapping("/login")
    public String login(@RequestBody User loginUser) {
        Optional<User> userOptional = Optional.of(userRepository.findByUsername(loginUser.getUsername()));
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(loginUser.getPassword(), user.getPassword())) {
                return "User logged in successfully";
            }
        }
        return "Invalid username or password";
    }

}

