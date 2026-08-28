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

    List<Booking> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Booking> findByEquipmentIdOrderByStartTimeAsc(Long equipmentId);
    List<Booking> findByStatusOrderByCreatedAtDesc(String status);
    List<Booking> findAllByOrderByCreatedAtDesc();

    long countByStatus(String status);

    // Conflict detection: overlapping active bookings
    @Query("SELECT b FROM Booking b WHERE b.equipment.id = :equipmentId " +
           "AND b.status IN ('APPROVED', 'PENDING') " +
           "AND (:startTime < b.endTime AND :endTime > b.startTime)")
    List<Booking> findConflictingBookings(
            @Param("equipmentId") Long equipmentId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    // Conflict detection excluding current booking (for updates)
    @Query("SELECT b FROM Booking b WHERE b.equipment.id = :equipmentId " +
           "AND b.status IN ('APPROVED', 'PENDING') " +
           "AND b.id != :excludeBookingId " +
           "AND (:startTime < b.endTime AND :endTime > b.startTime)")
    List<Booking> findConflictingBookingsExcluding(
            @Param("equipmentId") Long equipmentId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("excludeBookingId") Long excludeBookingId
    );

    // Top most booked equipment
    @Query("SELECT b.equipment.name, COUNT(b) FROM Booking b GROUP BY b.equipment.name ORDER BY COUNT(b) DESC")
    List<Object[]> findTopBookedEquipment();
}
