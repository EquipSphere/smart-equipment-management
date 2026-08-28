package com.equipsphere.service;

import com.equipsphere.dto.maintenance.MaintenanceRequestDTO;
import com.equipsphere.dto.maintenance.MaintenanceResolutionDTO;
import com.equipsphere.dto.maintenance.MaintenanceResponseDTO;

import java.util.List;

public interface MaintenanceService {

    MaintenanceResponseDTO reportMaintenance(MaintenanceRequestDTO requestDTO, String userEmail);

    List<MaintenanceResponseDTO> getAllMaintenance(String status);

    List<MaintenanceResponseDTO> getMaintenanceByEquipment(Long equipmentId);

    MaintenanceResponseDTO getMaintenanceById(Long id);

    MaintenanceResponseDTO updateMaintenanceStatus(Long id, MaintenanceResolutionDTO resolutionDTO);

    void deleteMaintenance(Long id);
}
