package edu.usta.groccy.repository;

import edu.usta.groccy.entity.Product;
import edu.usta.groccy.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByReference(String reference);

    List<Product> findAllByStatus(Status status);
}