package com.equipsphere.service;

import com.equipsphere.dto.equipment.EquipmentRequestDTO;
import com.equipsphere.dto.equipment.EquipmentResponseDTO;

import java.util.List;

public interface EquipmentService {
    List<EquipmentResponseDTO> getAllEquipment(String category, String status);
    EquipmentResponseDTO getEquipmentById(Long id);
    EquipmentResponseDTO addEquipment(EquipmentRequestDTO requestDTO);
    EquipmentResponseDTO updateEquipment(Long id, EquipmentRequestDTO requestDTO);
    EquipmentResponseDTO updateEquipmentStatus(Long id, String status);
    void deleteEquipment(Long id);
    List<EquipmentResponseDTO> searchEquipment(String keyword);
    List<String> getAllCategories();
}
