package edu.usta.groccy.repository;

import edu.usta.groccy.entity.RequiredMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequiredMaterialRepository extends JpaRepository<RequiredMaterial, Long> {

    List<RequiredMaterial> findAllByProductionSheetId(Long productionSheetId);
}