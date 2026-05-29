package edu.usta.groccy.repository;

import edu.usta.groccy.entity.Sale;
import edu.usta.groccy.enums.SaleStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    List<Sale> findAllByStatus(SaleStatus status);

    List<Sale> findAllByStoreId(Long storeId);

    List<Sale> findAllBySellerId(Long sellerId);
}