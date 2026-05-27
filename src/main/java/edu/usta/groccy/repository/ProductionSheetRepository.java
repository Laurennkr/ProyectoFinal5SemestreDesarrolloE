package edu.usta.groccy.repository;

import edu.usta.groccy.entity.ProductionSheet;
import edu.usta.groccy.enums.ProductionSheetStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductionSheetRepository extends JpaRepository<ProductionSheet, Long> {

    List<ProductionSheet> findAllByStatus(ProductionSheetStatus status);

    // Para el costurero: solo sus fichas asignadas
    List<ProductionSheet> findAllByAssignedTailorId(Long tailorId);

    // Para el costurero: sus fichas en un estado específico
    List<ProductionSheet> findAllByAssignedTailorIdAndStatus(
            Long tailorId, ProductionSheetStatus status);
}