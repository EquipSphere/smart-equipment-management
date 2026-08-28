package com.equipsphere.dto.maintenance;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceResolutionDTO {

    @NotBlank(message = "Status is required")
    private String status; // UNDER_MAINTENANCE, REPAIRED, CANCELLED

    private BigDecimal cost;
    private String technicianNotes;
}
