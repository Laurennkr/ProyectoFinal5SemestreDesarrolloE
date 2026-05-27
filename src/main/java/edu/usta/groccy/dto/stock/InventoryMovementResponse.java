package edu.usta.groccy.dto.stock;

import edu.usta.groccy.enums.MovementType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InventoryMovementResponse(
        Long id,
        MovementType movementType,
        BigDecimal quantity,
        LocalDateTime movementDate,
        String notes,
        Long productId,
        String productName,
        Long storeId,
        String storeName
) {}