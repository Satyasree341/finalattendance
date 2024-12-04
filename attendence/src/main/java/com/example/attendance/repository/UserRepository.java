package com.example.attendance.repository;

import com.example.attendance.model.Role;
import com.example.attendance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByRole(String role); // Finds users by role

    boolean existsByEmail(String email); // Check if email exists

    Optional<User> findByEmail(String email);
    long countByRoleNot(Role role);
}



