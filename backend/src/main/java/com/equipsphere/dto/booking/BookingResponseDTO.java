package com.equipsphere.dto.booking;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponseDTO {

    private Long id;
    private UserSummary user;
    private EquipmentSummary equipment;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    private String status;
    private String purpose;
    private String adminRemark;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserSummary {
        private Long id;
        private String name;
        private String email;
        private String department;
        private String phone;
        private String role;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class EquipmentSummary {
        private Long id;
        private String name;
        private String category;
        private String serialNumber;
        private String location;
        private String imageUrl;
        private String status;
    }
}
