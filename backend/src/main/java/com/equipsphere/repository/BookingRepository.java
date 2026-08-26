package com.equipsphere.repository;

import com.equipsphere.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserId(Long userId);
    List<Booking> findByEquipmentId(Long equipmentId);
    List<Booking> findByStatus(String status);

    long countByStatus(String status);

    // Check for overlapping bookings for conflict detection
    @Query("SELECT b FROM Booking b WHERE b.equipment.id = :equipmentId " +
           "AND b.status IN ('APPROVED', 'PENDING') " +
           "AND (:startTime < b.endTime AND :endTime > b.startTime)")
    List<Booking> findConflictingBookings(
            @Param("equipmentId") Long equipmentId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    // Top most booked equipment
    @Query("SELECT b.equipment.name, COUNT(b) FROM Booking b GROUP BY b.equipment.name ORDER BY COUNT(b) DESC")
    List<Object[]> findTopBookedEquipment();
}
