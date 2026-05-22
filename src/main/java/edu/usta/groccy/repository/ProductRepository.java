package edu.usta.groccy.repository;

import edu.usta.groccy.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByReference(String reference);
}