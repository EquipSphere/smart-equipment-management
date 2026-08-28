package com.equipsphere.dto.maintenance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceResponseDTO {

    private Long id;
    private Long equipmentId;
    private String equipmentName;
    private String equipmentCategory;
    private String equipmentSerialNumber;
    private String equipmentLocation;
    private String equipmentImageUrl;

    private Long reportedById;
    private String reportedByName;
    private String reportedByEmail;

    private String description;
    private String status; // REPORTED, UNDER_MAINTENANCE, REPAIRED, CANCELLED
    private BigDecimal cost;
    private String technicianNotes;
    private LocalDateTime reportedAt;
    private LocalDateTime resolvedAt;
}
