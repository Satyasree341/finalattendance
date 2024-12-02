

package com.example.attendance.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.attendance.model.AttendanceData;
import com.example.attendance.model.leaveRequest;
import com.example.attendance.model.User;
import com.example.attendance.service.AttendanceService;
import com.example.attendance.service.LeaveRequestService;
import com.example.attendance.service.UserService;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final AttendanceService attendanceService;
    private final LeaveRequestService leaveRequestService;

    @Autowired
    public AdminController(UserService userService, AttendanceService attendanceService, LeaveRequestService leaveRequestService) {
        this.userService = userService;
        this.attendanceService = attendanceService;
        this.leaveRequestService = leaveRequestService;
    }

    // Endpoint to get all users
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    // Endpoint to get specific user details by ID
    @GetMapping("/user/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> getUserDetails(@PathVariable Long id) {
        User user = userService.findUserById(id);
        return ResponseEntity.ok(user);
    }

    // Update user details
    @PutMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        User user = userService.updateUser(id, updatedUser);
        return ResponseEntity.ok(user);
    }

    // Delete a user
    @DeleteMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // Fetch all attendance records
    @GetMapping("/attendance/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AttendanceData>> getAllAttendanceRecords() {
        List<AttendanceData> records = attendanceService.findAllAttendanceRecords();
        return ResponseEntity.ok(records);
    }

    // Fetch attendance records for a specific user
    @GetMapping("/attendance/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AttendanceData>> getUserAttendanceRecords(@PathVariable Long id) {
        List<AttendanceData> records = attendanceService.findAttendanceRecordsByUserId(id);
        return ResponseEntity.ok(records);
    }

    // Fetch absentees for a specific date
    @GetMapping("/absentees")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAbsentees(@RequestParam("date") String date) {
        List<User> absentees = attendanceService.findAbsentees(date);
        return ResponseEntity.ok(absentees);
    }

    // Approve a leave request
    @PutMapping("/leave/approve/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> approveLeave(@PathVariable Long id) {
        leaveRequestService.approveLeave(id);
        return ResponseEntity.ok("Leave approved successfully");
    }

    // Fetch all leave requests
    @GetMapping("/leaves")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<leaveRequest>> getAllLeaveRequests() {
        List<leaveRequest> leaveRequests = leaveRequestService.getAllLeaveRequests();
        return ResponseEntity.ok(leaveRequests);
    }
}


