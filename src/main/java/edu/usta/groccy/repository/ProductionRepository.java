package edu.usta.groccy.repository;

import edu.usta.groccy.entity.Production;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductionRepository extends JpaRepository<Production, Long> {

    List<Production> findAllByProductionSheetId(Long productionSheetId);

    List<Production> findAllByTailorId(Long tailorId);
}