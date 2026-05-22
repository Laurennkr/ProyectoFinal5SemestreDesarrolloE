package edu.usta.groccy.dto.product;

import edu.usta.groccy.enums.Status;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String reference,
        String name,
        String size,
        String color,
        String category,
        BigDecimal salePrice,
        BigDecimal productionCost,
        Status status
) {
}