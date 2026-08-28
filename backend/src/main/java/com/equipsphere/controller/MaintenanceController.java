package com.equipsphere.controller;

import com.equipsphere.dto.maintenance.MaintenanceRequestDTO;
import com.equipsphere.dto.maintenance.MaintenanceResolutionDTO;
import com.equipsphere.dto.maintenance.MaintenanceResponseDTO;
import com.equipsphere.service.MaintenanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maintenance")
@RequiredArgsConstructor
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    @PostMapping
    public ResponseEntity<MaintenanceResponseDTO> reportMaintenance(
            @Valid @RequestBody MaintenanceRequestDTO requestDTO,
            Authentication authentication) {
        String email = authentication.getName();
        MaintenanceResponseDTO created = maintenanceService.reportMaintenance(requestDTO, email);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MaintenanceResponseDTO>> getAllMaintenance(
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(maintenanceService.getAllMaintenance(status));
    }

    @GetMapping("/equipment/{equipmentId}")
    public ResponseEntity<List<MaintenanceResponseDTO>> getMaintenanceByEquipment(
            @PathVariable Long equipmentId) {
        return ResponseEntity.ok(maintenanceService.getMaintenanceByEquipment(equipmentId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceResponseDTO> getMaintenanceById(@PathVariable Long id) {
        return ResponseEntity.ok(maintenanceService.getMaintenanceById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MaintenanceResponseDTO> updateMaintenanceStatus(
            @PathVariable Long id,
            @Valid @RequestBody MaintenanceResolutionDTO resolutionDTO) {
        return ResponseEntity.ok(maintenanceService.updateMaintenanceStatus(id, resolutionDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteMaintenance(@PathVariable Long id) {
        maintenanceService.deleteMaintenance(id);
        return ResponseEntity.ok("Maintenance ticket deleted successfully!");
    }
}
