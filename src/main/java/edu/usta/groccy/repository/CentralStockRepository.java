package edu.usta.groccy.repository;

import edu.usta.groccy.entity.CentralStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CentralStockRepository extends JpaRepository<CentralStock, Long> {

    Optional<CentralStock> findByProductId(Long productId);
}