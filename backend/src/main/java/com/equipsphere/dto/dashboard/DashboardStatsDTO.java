package com.equipsphere.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStatsDTO {

    // 4 Key Stat Cards
    private long totalEquipment;
    private long availableEquipment;
    private long bookedEquipment;
    private long underMaintenance;

    // Additional Core Counts
    private long pendingBookingsCount;
    private long approvedBookingsCount;
    private long totalUsersCount;
    private long activeMaintenanceCount;

    // Categorized Summary & Charts Data
    private Map<String, Long> equipmentByCategory;
    private List<MostBookedItemDTO> mostBookedEquipment;
}
