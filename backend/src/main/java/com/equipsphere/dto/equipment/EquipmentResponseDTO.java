package com.equipsphere.dto.equipment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipmentResponseDTO {
    private Long id;
    private String name;
    private String category;
    private String serialNumber;
    private String location;
    private String status;
    private String description;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
