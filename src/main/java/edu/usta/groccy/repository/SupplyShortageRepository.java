package edu.usta.groccy.repository;

import edu.usta.groccy.entity.SupplyShortage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplyShortageRepository extends JpaRepository<SupplyShortage, Long> {

    List<SupplyShortage> findAllByProductionSheetId(Long productionSheetId);

    List<SupplyShortage> findAllByTailorId(Long tailorId);
}