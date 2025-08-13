package com.thinkitive.thinkemr.repository;

import com.thinkitive.thinkemr.entity.Appointment;
import com.thinkitive.thinkemr.entity.AppointmentStatus;
import com.thinkitive.thinkemr.entity.AppointmentType;
import com.thinkitive.thinkemr.entity.AppointmentMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    
    @Query("SELECT a FROM Appointment a WHERE a.provider.id = :providerId " +
           "AND a.dateTime BETWEEN :startTime AND :endTime " +
           "AND a.isActive = true")
    List<Appointment> findConflictingAppointments(
            @Param("providerId") UUID providerId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
    
    @Query("SELECT COUNT(a) > 0 FROM Appointment a WHERE a.provider.id = :providerId " +
           "AND a.dateTime BETWEEN :startTime AND :endTime " +
           "AND a.isActive = true")
    boolean existsConflictingAppointment(
            @Param("providerId") UUID providerId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    @Query("SELECT a FROM Appointment a " +
           "JOIN FETCH a.patient p " +
           "JOIN FETCH a.provider pr " +
           "WHERE a.isActive = true")
    List<Appointment> findAllActiveAppointments();

    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.isActive = true")
    long countActiveAppointments();

    @Query("SELECT a.status, COUNT(a) FROM Appointment a WHERE a.isActive = true GROUP BY a.status")
    List<Object[]> getAppointmentStatusCounts();
} 