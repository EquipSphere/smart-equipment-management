package com.equipsphere.repository;

import com.equipsphere.entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    Optional<Equipment> findBySerialNumber(String serialNumber);
    Boolean existsBySerialNumber(String serialNumber);

    List<Equipment> findByStatus(String status);
    List<Equipment> findByCategory(String category);
    List<Equipment> findByCategoryAndStatus(String category, String status);

    long countByStatus(String status);

    @Query("SELECT DISTINCT e.category FROM Equipment e ORDER BY e.category ASC")
    List<String> findDistinctCategories();

    @Query("SELECT e FROM Equipment e WHERE " +
           "LOWER(e.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.category) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.location) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.serialNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Equipment> searchEquipment(@Param("keyword") String keyword);

    @Query("SELECT e.category, COUNT(e) FROM Equipment e GROUP BY e.category")
    List<Object[]> countEquipmentGroupedByCategory();
}
