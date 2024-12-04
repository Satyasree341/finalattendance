package com.example.attendance.repository;

import com.example.attendance.model.AttendanceData;
import com.example.attendance.model.User;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<AttendanceData, Long> {

    // boolean existsByUserAndLoginOptionAndLoginTimeAfter(User user, String loginOption, Date startOfDay);
    // List<AttendanceData> findByUserId(Long userId);

    // DP UPDATE BELOW CODE 
 @Query("SELECT a FROM AttendanceData a WHERE a.user.id = :userId " +
                        "AND DATE(a.loginTime) = :date ORDER BY a.loginTime DESC limit 1")
        AttendanceData findTopByUserIdAndLoginDateOrderByLoginTimeDesc(
                        @Param("userId") Integer userId,

                        @Param("date") LocalDate date);

        List<AttendanceData> findByUserId(Long id);

        boolean existsByUserAndLoginOptionAndLoginTimeAfterAndInstituteId(User user, String loginOption,
                        Date startOfDay,
                        Long instituteId);
}
    
}
