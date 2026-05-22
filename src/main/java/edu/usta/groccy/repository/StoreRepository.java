package edu.usta.groccy.repository;

import edu.usta.groccy.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
}