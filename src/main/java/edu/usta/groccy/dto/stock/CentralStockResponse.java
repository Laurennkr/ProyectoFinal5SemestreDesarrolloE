package edu.usta.groccy.dto.stock;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CentralStockResponse(
        Long id,
        Long productId,
        String productName,
        String productReference,
        BigDecimal availableQuantity,
        LocalDateTime lastUpdated
) {}