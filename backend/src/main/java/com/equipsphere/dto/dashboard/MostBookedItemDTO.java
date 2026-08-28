package com.equipsphere.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MostBookedItemDTO {
    private String equipmentName;
    private Long bookingCount;
}
