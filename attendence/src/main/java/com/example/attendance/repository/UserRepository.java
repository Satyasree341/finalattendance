

/*package com.example.attendance.repository;

import com.example.attendance.model.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    // Find all users except those with the "ADMIN" role
    List<User> findByRoleNot(String role);

    // Find a user by email, return Optional to handle null safely
    Optional<User> findByEmail(String email);

    // Find a user by username, return Optional to handle null safely
    Optional<User> findByUsername(String username);

    // Find users by role, return Optional to handle cases with no users found
    Optional<User> findByRole(String role);

    // Check if a user with a specific email exists
    boolean existsByEmail(String email);

    // Check if a user with a specific username exists
    boolean existsByUsername(String username);
}*/

package com.example.attendance.repository;

import com.example.attendance.model.User;
import com.example.attendance.model.Role;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    // Find all users except admins
    List<User> findByRoleNot(Role role);
    
    // Find a user by email
    Optional<User> findByEmail(String email);
    
    // Find a user by username
    Optional<User> findByUsername(String username);
    
    // Find users by role
    Optional<User> findByRole(String role);
    
    // Check if email exists
    boolean existsByEmail(String email);
    
    // Check if username exists
    boolean existsByUsername(String username);
    
    // Find all non-admin users
    @Query("SELECT u FROM User u WHERE u.role != 'ADMIN'")
    List<User> findAllNonAdminUsers();
}


