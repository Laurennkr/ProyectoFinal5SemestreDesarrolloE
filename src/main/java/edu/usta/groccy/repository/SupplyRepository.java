package edu.usta.groccy.repository;

import edu.usta.groccy.entity.Supply;
import edu.usta.groccy.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplyRepository extends JpaRepository<Supply, Long> {

    boolean existsByNameAndSupplierId(String name, Long supplierId);

    List<Supply> findAllByStatus(Status status);
}