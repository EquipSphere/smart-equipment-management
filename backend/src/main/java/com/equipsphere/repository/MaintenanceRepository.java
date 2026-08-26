package com.equipsphere.repository;

import com.equipsphere.entity.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {
    List<Maintenance> findByEquipmentId(Long equipmentId);
    List<Maintenance> findByStatus(String status);
    long countByStatus(String status);
}
