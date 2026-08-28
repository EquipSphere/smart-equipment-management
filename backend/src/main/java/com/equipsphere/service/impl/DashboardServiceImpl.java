package com.equipsphere.service.impl;

import com.equipsphere.dto.dashboard.DashboardStatsDTO;
import com.equipsphere.dto.dashboard.MostBookedItemDTO;
import com.equipsphere.repository.BookingRepository;
import com.equipsphere.repository.EquipmentRepository;
import com.equipsphere.repository.MaintenanceRepository;
import com.equipsphere.repository.UserRepository;
import com.equipsphere.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final EquipmentRepository equipmentRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final MaintenanceRepository maintenanceRepository;

    @Override
    public DashboardStatsDTO getAdminDashboardStats() {
        long totalEquipment = equipmentRepository.count();
        long availableEquipment = equipmentRepository.countByStatus("AVAILABLE");
        long bookedEquipment = equipmentRepository.countByStatus("BOOKED");
        long underMaintenance = equipmentRepository.countByStatus("MAINTENANCE");

        long pendingBookings = bookingRepository.countByStatus("PENDING");
        long approvedBookings = bookingRepository.countByStatus("APPROVED");
        long totalUsers = userRepository.count();
        long activeMaintenance = maintenanceRepository.countByStatus("REPORTED") +
                                 maintenanceRepository.countByStatus("UNDER_MAINTENANCE");

        // Equipment grouped by Category for Charts / Stats
        Map<String, Long> equipmentByCategory = new HashMap<>();
        List<Object[]> categoryCounts = equipmentRepository.countEquipmentGroupedByCategory();
        for (Object[] row : categoryCounts) {
            String category = (String) row[0];
            Long count = (Long) row[1];
            equipmentByCategory.put(category, count);
        }

        // Top Most Booked Equipment list
        List<MostBookedItemDTO> mostBooked = new ArrayList<>();
        List<Object[]> topBookedRows = bookingRepository.findTopBookedEquipment();
        for (Object[] row : topBookedRows) {
            String equipmentName = (String) row[0];
            Long count = (Long) row[1];
            mostBooked.add(new MostBookedItemDTO(equipmentName, count));
        }

        return DashboardStatsDTO.builder()
                .totalEquipment(totalEquipment)
                .availableEquipment(availableEquipment)
                .bookedEquipment(bookedEquipment)
                .underMaintenance(underMaintenance)
                .pendingBookingsCount(pendingBookings)
                .approvedBookingsCount(approvedBookings)
                .totalUsersCount(totalUsers)
                .activeMaintenanceCount(activeMaintenance)
                .equipmentByCategory(equipmentByCategory)
                .mostBookedEquipment(mostBooked)
                .build();
    }
}
