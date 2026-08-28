package com.equipsphere.dto.equipment;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipmentRequestDTO {

    @NotBlank(message = "Equipment name is required")
    private String name;

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Serial number is required")
    private String serialNumber;

    @NotBlank(message = "Location is required")
    private String location;

    @Builder.Default
    private String status = "AVAILABLE"; // "AVAILABLE", "BOOKED", "MAINTENANCE"

    private String description;
    private String imageUrl;
}
