package edu.usta.groccy.dto.sale;

import java.math.BigDecimal;

public record SaleDetailResponse(
        Long id,
        Long productId,
        String productReference,
        String productName,
        BigDecimal quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {
}