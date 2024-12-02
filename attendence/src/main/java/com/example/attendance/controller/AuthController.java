package com.example.attendance.controller;

import com.example.attendance.Util.JwtUtil;
import com.example.attendance.model.Role;
import com.example.attendance.model.User;
import com.example.attendance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody User user) {
        System.out.println("Received login request for email: " + user.getEmail());

        // Find user by email (returns Optional<User>)
        Optional<User> foundUserOpt = userRepository.findByEmail(user.getEmail());
        
        if (!foundUserOpt.isPresent()) {
            System.out.println("User not found for email: " + user.getEmail());
            return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials"));
        }

        User foundUser = foundUserOpt.get();  // Get the user from the Optional

        System.out.println("Stored password hash: " + foundUser.getPassword());
        System.out.println("Entered password: " + user.getPassword());

        // Check password
        if (passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
            System.out.println("Password matches!");
            String token = jwtUtil.generateToken(foundUser.getEmail());

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("token", token);
            response.put("id", foundUser.getId());
            response.put("role", foundUser.getRole());
            System.out.println("Generated token: " + token);
            return ResponseEntity.ok(response);
        } else {
            System.out.println("Passwords do not match.");
            return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();

        // Check if email already exists
        Optional<User> existingUserOpt = userRepository.findByEmail(user.getEmail());
        if (existingUserOpt.isPresent()) {
            response.put("message", "Email already exists");
            return ResponseEntity.status(409).body(response);
        }

        // Check if admin already exists
        if (user.getRole() == Role.ADMIN) {
            Optional<User> adminOpt = userRepository.findByRole("ADMIN");
            if (adminOpt.isPresent()) {
                response.put("message", "An admin already exists. Only one admin can be registered.");
                return ResponseEntity.status(400).body(response);
            }
        }

        // Set default role if not specified
        if (user.getRole() == null) {
            user.setRole(Role.USER); // Default role
        }

        // Hash password and save user
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);

        response.put("message", "Registration successful");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-admin")
    public ResponseEntity<Boolean> checkAdminExists() {
        Optional<User> adminOpt = userRepository.findByRole("ADMIN");
        boolean adminExists = adminOpt.isPresent();
        return ResponseEntity.ok(adminExists);
    }
}
