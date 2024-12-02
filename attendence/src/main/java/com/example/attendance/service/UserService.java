package com.example.attendance.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.attendance.model.User;
import com.example.attendance.model.Role;
import com.example.attendance.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public void registerUser(String email, String password, Role role) {
        String hashedPassword = passwordEncoder.encode(password);
        User user = new User(email, hashedPassword, role, hashedPassword);
        userRepository.save(user);
    }

    public boolean loginUser(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return passwordEncoder.matches(password, user.getPassword());
        }
        return false; // User not found, return false
    }

    public boolean isAdminExists() {
        Optional<User> admins = userRepository.findByRole("ADMIN");
        return admins.isPresent();
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null); // Return null if not found
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(Long id, User updatedUser) {
        Optional<User> existingUserOpt = userRepository.findById(id);
        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword())); // Re-encode password
            existingUser.setRole(updatedUser.getRole());
            return userRepository.save(existingUser);
        }
        return null; // Return null if the user doesn't exist
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}

