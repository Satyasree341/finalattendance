package com.example.attendance.service;

import com.example.attendance.model.Role;
import com.example.attendance.model.User;
import com.example.attendance.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Check if the email already exists
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    // Find a user by role
    public Optional<User> findByRole(String role) {
        return userRepository.findByRole(role);
    }

    // Get all users
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
    

    // Find user by email
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    // Update user details
    public User updateUser(Long id, User updatedUser) {
        Optional<User> existingUserOpt = userRepository.findById(id);
        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();
            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setRole(updatedUser.getRole());
            existingUser.setCourse(updatedUser.getCourse());
            return userRepository.save(existingUser);
        }
        return null;
    }

    // Delete a user by ID
    public void deleteUser(Long id) {
        userRepository.deleteById(id);

    }
    public long countByRoleNot(Role role) {
        return userRepository.countByRoleNot(role);  // Passing Role enum
    }
}



