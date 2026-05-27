package edu.usta.groccy.repository;

import edu.usta.groccy.entity.InventoryMovement;
import edu.usta.groccy.enums.MovementType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, Long> {

    List<InventoryMovement> findAllByProductId(Long productId);

    List<InventoryMovement> findAllByStoreId(Long storeId);

    List<InventoryMovement> findAllByMovementType(MovementType movementType);
}