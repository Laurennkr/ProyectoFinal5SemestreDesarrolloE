package edu.usta.groccy.repository;

import edu.usta.groccy.entity.StoreStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoreStockRepository extends JpaRepository<StoreStock, Long> {

    Optional<StoreStock> findByProductIdAndStoreId(Long productId, Long storeId);

    List<StoreStock> findAllByStoreId(Long storeId);

    List<StoreStock> findAllByProductId(Long productId);
}