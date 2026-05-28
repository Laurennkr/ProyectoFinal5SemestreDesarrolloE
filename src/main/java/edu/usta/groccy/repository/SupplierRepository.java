package edu.usta.groccy.repository;

import edu.usta.groccy.entity.Supplier;
import edu.usta.groccy.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    boolean existsByEmail(String email);

    List<Supplier> findAllByStatus(Status status);
}