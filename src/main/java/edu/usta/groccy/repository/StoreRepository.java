package edu.usta.groccy.repository;

import edu.usta.groccy.entity.Store;
import edu.usta.groccy.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    List<Store> findAllByStatus(Status status);
}