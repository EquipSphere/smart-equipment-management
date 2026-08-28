package com.equipsphere.controller;

import com.equipsphere.dto.booking.*;
import com.equipsphere.security.services.UserDetailsImpl;
import com.equipsphere.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BookingResponseDTO> createBooking(
            @Valid @RequestBody BookingRequestDTO request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        BookingResponseDTO response = bookingService.createBooking(request, userDetails.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/check-availability")
    public ResponseEntity<AvailabilityCheckDTO.Response> checkAvailability(
            @Valid @RequestBody AvailabilityCheckDTO.Request request
    ) {
        AvailabilityCheckDTO.Response response = bookingService.checkAvailability(
                request.getEquipmentId(),
                request.getStartTime(),
                request.getEndTime()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<BookingResponseDTO>> getMyBookings(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<BookingResponseDTO> response = bookingService.getUserBookings(userDetails.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BookingResponseDTO>> getAllBookings(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long equipmentId
    ) {
        List<BookingResponseDTO> response = bookingService.getAllBookings(status, equipmentId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/equipment/{equipmentId}/schedule")
    public ResponseEntity<List<BookingResponseDTO>> getEquipmentSchedule(
            @PathVariable Long equipmentId
    ) {
        List<BookingResponseDTO> response = bookingService.getAllBookings(null, equipmentId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BookingResponseDTO> getBookingById(@PathVariable Long id) {
        BookingResponseDTO response = bookingService.getBookingById(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BookingResponseDTO> updateBookingStatus(
            @PathVariable Long id,
            @Valid @RequestBody BookingStatusUpdateDTO dto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        BookingResponseDTO response = bookingService.updateBookingStatus(id, dto, userDetails.getId(), isAdmin);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> cancelBooking(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        bookingService.cancelBooking(id, userDetails.getId(), isAdmin);
        return ResponseEntity.noContent().build();
    }
}
