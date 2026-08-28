package com.equipsphere.service.impl;

import com.equipsphere.dto.equipment.EquipmentRequestDTO;
import com.equipsphere.dto.equipment.EquipmentResponseDTO;
import com.equipsphere.entity.Equipment;
import com.equipsphere.exception.ResourceNotFoundException;
import com.equipsphere.repository.EquipmentRepository;
import com.equipsphere.service.EquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EquipmentServiceImpl implements EquipmentService {

    private final EquipmentRepository equipmentRepository;

    @Override
    public List<EquipmentResponseDTO> getAllEquipment(String category, String status) {
        List<Equipment> list;
        if (category != null && !category.trim().isEmpty() && status != null && !status.trim().isEmpty()) {
            list = equipmentRepository.findByCategoryAndStatus(category, status);
        } else if (category != null && !category.trim().isEmpty()) {
            list = equipmentRepository.findByCategory(category);
        } else if (status != null && !status.trim().isEmpty()) {
            list = equipmentRepository.findByStatus(status);
        } else {
            list = equipmentRepository.findAll();
        }

        return list.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public EquipmentResponseDTO getEquipmentById(Long id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with id: " + id));
        return mapToDTO(equipment);
    }

    @Override
    public EquipmentResponseDTO addEquipment(EquipmentRequestDTO requestDTO) {
        if (equipmentRepository.existsBySerialNumber(requestDTO.getSerialNumber())) {
            throw new IllegalArgumentException("Equipment with serial number '" + requestDTO.getSerialNumber() + "' already exists.");
        }

        Equipment equipment = Equipment.builder()
                .name(requestDTO.getName())
                .category(requestDTO.getCategory())
                .serialNumber(requestDTO.getSerialNumber())
                .location(requestDTO.getLocation())
                .status(requestDTO.getStatus() != null ? requestDTO.getStatus() : "AVAILABLE")
                .description(requestDTO.getDescription())
                .imageUrl(requestDTO.getImageUrl())
                .build();

        Equipment saved = equipmentRepository.save(equipment);
        return mapToDTO(saved);
    }

    @Override
    public EquipmentResponseDTO updateEquipment(Long id, EquipmentRequestDTO requestDTO) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with id: " + id));

        // If serial number changed, verify uniqueness
        if (!equipment.getSerialNumber().equals(requestDTO.getSerialNumber()) &&
                equipmentRepository.existsBySerialNumber(requestDTO.getSerialNumber())) {
            throw new IllegalArgumentException("Equipment with serial number '" + requestDTO.getSerialNumber() + "' already exists.");
        }

        equipment.setName(requestDTO.getName());
        equipment.setCategory(requestDTO.getCategory());
        equipment.setSerialNumber(requestDTO.getSerialNumber());
        equipment.setLocation(requestDTO.getLocation());
        if (requestDTO.getStatus() != null) {
            equipment.setStatus(requestDTO.getStatus());
        }
        equipment.setDescription(requestDTO.getDescription());
        equipment.setImageUrl(requestDTO.getImageUrl());

        Equipment updated = equipmentRepository.save(equipment);
        return mapToDTO(updated);
    }

    @Override
    public EquipmentResponseDTO updateEquipmentStatus(Long id, String status) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with id: " + id));

        equipment.setStatus(status);
        Equipment updated = equipmentRepository.save(equipment);
        return mapToDTO(updated);
    }

    @Override
    public void deleteEquipment(Long id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with id: " + id));
        equipmentRepository.delete(equipment);
    }

    @Override
    public List<EquipmentResponseDTO> searchEquipment(String keyword) {
        return equipmentRepository.searchEquipment(keyword).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllCategories() {
        return equipmentRepository.findDistinctCategories();
    }

    private EquipmentResponseDTO mapToDTO(Equipment equipment) {
        return EquipmentResponseDTO.builder()
                .id(equipment.getId())
                .name(equipment.getName())
                .category(equipment.getCategory())
                .serialNumber(equipment.getSerialNumber())
                .location(equipment.getLocation())
                .status(equipment.getStatus())
                .description(equipment.getDescription())
                .imageUrl(equipment.getImageUrl())
                .createdAt(equipment.getCreatedAt())
                .updatedAt(equipment.getUpdatedAt())
                .build();
    }
}
