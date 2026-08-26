package com.equipsphere.controller;

import com.equipsphere.dto.equipment.EquipmentRequestDTO;
import com.equipsphere.dto.equipment.EquipmentResponseDTO;
import com.equipsphere.service.EquipmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment")
@RequiredArgsConstructor
public class EquipmentController {

    private final EquipmentService equipmentService;

    @GetMapping
    public ResponseEntity<List<EquipmentResponseDTO>> getAllEquipment(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(equipmentService.getAllEquipment(category, status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipmentResponseDTO> getEquipmentById(@PathVariable Long id) {
        return ResponseEntity.ok(equipmentService.getEquipmentById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<EquipmentResponseDTO>> searchEquipment(@RequestParam String keyword) {
        return ResponseEntity.ok(equipmentService.searchEquipment(keyword));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories() {
        return ResponseEntity.ok(equipmentService.getAllCategories());
    }

    @PostMapping
    public ResponseEntity<EquipmentResponseDTO> addEquipment(@Valid @RequestBody EquipmentRequestDTO requestDTO) {
        EquipmentResponseDTO created = equipmentService.addEquipment(requestDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipmentResponseDTO> updateEquipment(
            @PathVariable Long id,
            @Valid @RequestBody EquipmentRequestDTO requestDTO) {
        return ResponseEntity.ok(equipmentService.updateEquipment(id, requestDTO));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<EquipmentResponseDTO> updateEquipmentStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(equipmentService.updateEquipmentStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEquipment(@PathVariable Long id) {
        equipmentService.deleteEquipment(id);
        return ResponseEntity.ok("Equipment deleted successfully!");
    }
}
