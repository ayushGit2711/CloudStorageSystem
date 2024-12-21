package com.ayushspring.cloudstoragesystem.service;

import com.ayushspring.cloudstoragesystem.dto.LoginRequest;
import com.ayushspring.cloudstoragesystem.dto.RegisterRequest;
import com.ayushspring.cloudstoragesystem.model.User;
import com.ayushspring.cloudstoragesystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void registerUser(RegisterRequest registerRequest) {
        // Check if username already exists
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new RuntimeException("Username already taken");
        }

        // Create user and encode password
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        // Save user to database
        userRepository.save(user);
    }

    public String loginUser(LoginRequest loginRequest) {
        // Check if username exists
        Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());
        if (!userOpt.isPresent()) {
            throw new RuntimeException("User not found");
        }

        User user = userOpt.get();

        // Check if password matches
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // Generate JWT token (assuming you implement JWT generation)
        // For now, let's just return a dummy token
        return "dummy-jwt-token";  // You can replace this with actual JWT generation logic
    }
}
