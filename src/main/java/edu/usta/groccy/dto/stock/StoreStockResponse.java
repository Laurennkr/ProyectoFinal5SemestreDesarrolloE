package edu.usta.groccy.dto.stock;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record StoreStockResponse(
        Long id,
        Long productId,
        String productName,
        String productReference,
        Long storeId,
        String storeName,
        BigDecimal availableQuantity,
        BigDecimal minimumStock,
        LocalDateTime lastUpdated
) {}