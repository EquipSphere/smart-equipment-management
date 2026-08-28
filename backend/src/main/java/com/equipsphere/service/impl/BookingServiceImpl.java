package com.equipsphere.service.impl;

import com.equipsphere.dto.booking.*;
import com.equipsphere.entity.Booking;
import com.equipsphere.entity.Equipment;
import com.equipsphere.entity.User;
import com.equipsphere.exception.ResourceNotFoundException;
import com.equipsphere.repository.BookingRepository;
import com.equipsphere.repository.EquipmentRepository;
import com.equipsphere.repository.UserRepository;
import com.equipsphere.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final EquipmentRepository equipmentRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public BookingResponseDTO createBooking(BookingRequestDTO request, Long userId) {
        // 1. Validate Time Logic
        if (request.getStartTime() == null || request.getEndTime() == null) {
            throw new IllegalArgumentException("Start time and End time must be provided");
        }

        if (!request.getEndTime().isAfter(request.getStartTime())) {
            throw new IllegalArgumentException("End time must be strictly after start time");
        }

        if (request.getStartTime().isBefore(LocalDateTime.now().minusMinutes(5))) {
            throw new IllegalArgumentException("Reservation start time cannot be in the past");
        }

        // 2. Fetch and Validate User
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        // 3. Fetch and Validate Equipment
        Equipment equipment = equipmentRepository.findById(request.getEquipmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with ID: " + request.getEquipmentId()));

        if ("MAINTENANCE".equalsIgnoreCase(equipment.getStatus()) || "RETIRED".equalsIgnoreCase(equipment.getStatus())) {
            throw new IllegalArgumentException("Equipment is currently unavailable (" + equipment.getStatus() + ") and cannot be booked.");
        }

        // 4. Automated Conflict Detection Check
        List<Booking> conflicts = bookingRepository.findConflictingBookings(
                request.getEquipmentId(),
                request.getStartTime(),
                request.getEndTime()
        );

        if (!conflicts.isEmpty()) {
            throw new IllegalArgumentException("Time slot conflict: Equipment '" + equipment.getName() + 
                    "' is already reserved or pending reservation during this time interval.");
        }

        // 5. Save Booking
        Booking booking = Booking.builder()
                .user(user)
                .equipment(equipment)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .purpose(request.getPurpose())
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Booking savedBooking = bookingRepository.save(booking);
        return mapToDTO(savedBooking);
    }

    @Override
    @Transactional(readOnly = true)
    public AvailabilityCheckDTO.Response checkAvailability(Long equipmentId, LocalDateTime startTime, LocalDateTime endTime) {
        if (!equipmentRepository.existsById(equipmentId)) {
            throw new ResourceNotFoundException("Equipment not found with ID: " + equipmentId);
        }

        if (startTime == null || endTime == null || !endTime.isAfter(startTime)) {
            return AvailabilityCheckDTO.Response.builder()
                    .isAvailable(false)
                    .equipmentId(equipmentId)
                    .message("Invalid time range specified.")
                    .conflictingSlots(List.of())
                    .build();
        }

        List<Booking> conflicts = bookingRepository.findConflictingBookings(equipmentId, startTime, endTime);

        if (conflicts.isEmpty()) {
            return AvailabilityCheckDTO.Response.builder()
                    .isAvailable(true)
                    .equipmentId(equipmentId)
                    .message("Equipment is completely available for the selected time slot.")
                    .conflictingSlots(List.of())
                    .build();
        }

        List<AvailabilityCheckDTO.ConflictSlot> conflictSlots = conflicts.stream()
                .map(b -> AvailabilityCheckDTO.ConflictSlot.builder()
                        .bookingId(b.getId())
                        .startTime(b.getStartTime())
                        .endTime(b.getEndTime())
                        .status(b.getStatus())
                        .build())
                .collect(Collectors.toList());

        return AvailabilityCheckDTO.Response.builder()
                .isAvailable(false)
                .equipmentId(equipmentId)
                .message("Equipment has " + conflicts.size() + " conflicting reservation(s) during this time.")
                .conflictingSlots(conflictSlots)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getAllBookings(String status, Long equipmentId) {
        List<Booking> bookings;

        if (status != null && !status.isBlank()) {
            bookings = bookingRepository.findByStatusOrderByCreatedAtDesc(status.toUpperCase().trim());
        } else if (equipmentId != null) {
            bookings = bookingRepository.findByEquipmentIdOrderByStartTimeAsc(equipmentId);
        } else {
            bookings = bookingRepository.findAllByOrderByCreatedAtDesc();
        }

        return bookings.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getUserBookings(Long userId) {
        return bookingRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponseDTO getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + id));
        return mapToDTO(booking);
    }

    @Override
    @Transactional
    public BookingResponseDTO updateBookingStatus(Long bookingId, BookingStatusUpdateDTO dto, Long currentUserId, boolean isAdmin) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + bookingId));

        String newStatus = dto.getStatus().toUpperCase().trim();

        // Permissions check
        if (("APPROVED".equals(newStatus) || "REJECTED".equals(newStatus)) && !isAdmin) {
            throw new IllegalArgumentException("Only administrators can approve or reject booking requests.");
        }

        if ("CANCELLED".equals(newStatus) && !isAdmin && !booking.getUser().getId().equals(currentUserId)) {
            throw new IllegalArgumentException("You can only cancel your own bookings.");
        }

        // Re-check conflict if approving
        if ("APPROVED".equals(newStatus)) {
            List<Booking> conflicts = bookingRepository.findConflictingBookingsExcluding(
                    booking.getEquipment().getId(),
                    booking.getStartTime(),
                    booking.getEndTime(),
                    booking.getId()
            );

            long approvedConflicts = conflicts.stream().filter(b -> "APPROVED".equalsIgnoreCase(b.getStatus())).count();
            if (approvedConflicts > 0) {
                throw new IllegalArgumentException("Cannot approve: Another booking was already approved for this time slot!");
            }
        }

        booking.setStatus(newStatus);
        if (dto.getAdminRemark() != null) {
            booking.setAdminRemark(dto.getAdminRemark());
        }
        booking.setUpdatedAt(LocalDateTime.now());

        return mapToDTO(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public void cancelBooking(Long bookingId, Long currentUserId, boolean isAdmin) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + bookingId));

        if (!isAdmin && !booking.getUser().getId().equals(currentUserId)) {
            throw new IllegalArgumentException("You are not authorized to cancel this booking.");
        }

        if ("COMPLETED".equalsIgnoreCase(booking.getStatus())) {
            throw new IllegalArgumentException("Cannot cancel a completed booking.");
        }

        booking.setStatus("CANCELLED");
        booking.setUpdatedAt(LocalDateTime.now());
        bookingRepository.save(booking);
    }

    private BookingResponseDTO mapToDTO(Booking b) {
        return BookingResponseDTO.builder()
                .id(b.getId())
                .user(BookingResponseDTO.UserSummary.builder()
                        .id(b.getUser().getId())
                        .name(b.getUser().getName())
                        .email(b.getUser().getEmail())
                        .department(b.getUser().getDepartment())
                        .phone(b.getUser().getPhone())
                        .role(b.getUser().getRole())
                        .build())
                .equipment(BookingResponseDTO.EquipmentSummary.builder()
                        .id(b.getEquipment().getId())
                        .name(b.getEquipment().getName())
                        .category(b.getEquipment().getCategory())
                        .serialNumber(b.getEquipment().getSerialNumber())
                        .location(b.getEquipment().getLocation())
                        .imageUrl(b.getEquipment().getImageUrl())
                        .status(b.getEquipment().getStatus())
                        .build())
                .startTime(b.getStartTime())
                .endTime(b.getEndTime())
                .status(b.getStatus())
                .purpose(b.getPurpose())
                .adminRemark(b.getAdminRemark())
                .createdAt(b.getCreatedAt())
                .updatedAt(b.getUpdatedAt())
                .build();
    }
}
