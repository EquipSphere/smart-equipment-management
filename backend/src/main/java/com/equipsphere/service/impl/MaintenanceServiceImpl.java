package com.equipsphere.service.impl;

import com.equipsphere.dto.maintenance.MaintenanceRequestDTO;
import com.equipsphere.dto.maintenance.MaintenanceResolutionDTO;
import com.equipsphere.dto.maintenance.MaintenanceResponseDTO;
import com.equipsphere.entity.Equipment;
import com.equipsphere.entity.Maintenance;
import com.equipsphere.entity.User;
import com.equipsphere.exception.ResourceNotFoundException;
import com.equipsphere.repository.EquipmentRepository;
import com.equipsphere.repository.MaintenanceRepository;
import com.equipsphere.repository.UserRepository;
import com.equipsphere.service.MaintenanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MaintenanceServiceImpl implements MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;
    private final EquipmentRepository equipmentRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public MaintenanceResponseDTO reportMaintenance(MaintenanceRequestDTO requestDTO, String userEmail) {
        Equipment equipment = equipmentRepository.findById(requestDTO.getEquipmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with id: " + requestDTO.getEquipmentId()));

        User reporter = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userEmail));

        // 1. Create Maintenance record
        Maintenance maintenance = Maintenance.builder()
                .equipment(equipment)
                .reportedBy(reporter)
                .description(requestDTO.getDescription())
                .status("REPORTED")
                .cost(BigDecimal.ZERO)
                .reportedAt(LocalDateTime.now())
                .build();

        Maintenance saved = maintenanceRepository.save(maintenance);

        // 2. Automatically switch equipment status to MAINTENANCE
        equipment.setStatus("MAINTENANCE");
        equipmentRepository.save(equipment);

        return mapToDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaintenanceResponseDTO> getAllMaintenance(String status) {
        List<Maintenance> list;
        if (status != null && !status.trim().isEmpty() && !status.equalsIgnoreCase("ALL")) {
            list = maintenanceRepository.findByStatus(status.toUpperCase());
        } else {
            list = maintenanceRepository.findAll();
        }
        return list.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaintenanceResponseDTO> getMaintenanceByEquipment(Long equipmentId) {
        return maintenanceRepository.findByEquipmentId(equipmentId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public MaintenanceResponseDTO getMaintenanceById(Long id) {
        Maintenance maintenance = maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance record not found with id: " + id));
        return mapToDTO(maintenance);
    }

    @Override
    @Transactional
    public MaintenanceResponseDTO updateMaintenanceStatus(Long id, MaintenanceResolutionDTO resolutionDTO) {
        Maintenance maintenance = maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance record not found with id: " + id));

        String newStatus = resolutionDTO.getStatus().toUpperCase();
        maintenance.setStatus(newStatus);

        if (resolutionDTO.getCost() != null) {
            maintenance.setCost(resolutionDTO.getCost());
        }

        if (resolutionDTO.getTechnicianNotes() != null) {
            maintenance.setTechnicianNotes(resolutionDTO.getTechnicianNotes());
        }

        Equipment equipment = maintenance.getEquipment();

        // If ticket is resolved or cancelled, restore equipment to AVAILABLE
        if ("REPAIRED".equals(newStatus) || "CANCELLED".equals(newStatus)) {
            maintenance.setResolvedAt(LocalDateTime.now());
            if (equipment != null) {
                equipment.setStatus("AVAILABLE");
                equipmentRepository.save(equipment);
            }
        } else if ("UNDER_MAINTENANCE".equals(newStatus) || "REPORTED".equals(newStatus)) {
            if (equipment != null) {
                equipment.setStatus("MAINTENANCE");
                equipmentRepository.save(equipment);
            }
        }

        Maintenance updated = maintenanceRepository.save(maintenance);
        return mapToDTO(updated);
    }

    @Override
    @Transactional
    public void deleteMaintenance(Long id) {
        Maintenance maintenance = maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance record not found with id: " + id));
        maintenanceRepository.delete(maintenance);
    }

    private MaintenanceResponseDTO mapToDTO(Maintenance m) {
        Equipment eq = m.getEquipment();
        User reporter = m.getReportedBy();

        return MaintenanceResponseDTO.builder()
                .id(m.getId())
                .equipmentId(eq != null ? eq.getId() : null)
                .equipmentName(eq != null ? eq.getName() : "Unknown")
                .equipmentCategory(eq != null ? eq.getCategory() : "N/A")
                .equipmentSerialNumber(eq != null ? eq.getSerialNumber() : "N/A")
                .equipmentLocation(eq != null ? eq.getLocation() : "N/A")
                .equipmentImageUrl(eq != null ? eq.getImageUrl() : null)
                .reportedById(reporter != null ? reporter.getId() : null)
                .reportedByName(reporter != null ? reporter.getName() : "Unknown")
                .reportedByEmail(reporter != null ? reporter.getEmail() : "N/A")
                .description(m.getDescription())
                .status(m.getStatus())
                .cost(m.getCost())
                .technicianNotes(m.getTechnicianNotes())
                .reportedAt(m.getReportedAt())
                .resolvedAt(m.getResolvedAt())
                .build();
    }
}
