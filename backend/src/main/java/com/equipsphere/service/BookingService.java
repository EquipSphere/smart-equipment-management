package com.equipsphere.service;

import com.equipsphere.dto.booking.*;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {

    BookingResponseDTO createBooking(BookingRequestDTO request, Long userId);

    AvailabilityCheckDTO.Response checkAvailability(Long equipmentId, LocalDateTime startTime, LocalDateTime endTime);

    List<BookingResponseDTO> getAllBookings(String status, Long equipmentId);

    List<BookingResponseDTO> getUserBookings(Long userId);

    BookingResponseDTO getBookingById(Long id);

    BookingResponseDTO updateBookingStatus(Long bookingId, BookingStatusUpdateDTO dto, Long currentUserId, boolean isAdmin);

    void cancelBooking(Long bookingId, Long currentUserId, boolean isAdmin);
}
