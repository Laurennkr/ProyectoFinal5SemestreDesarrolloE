package edu.usta.groccy.repository;

import edu.usta.groccy.entity.SaleDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleDetailRepository extends JpaRepository<SaleDetail, Long> {

    List<SaleDetail> findAllBySaleId(Long saleId);

    List<SaleDetail> findAllByProductId(Long productId);
}