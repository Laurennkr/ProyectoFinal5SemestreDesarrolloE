package edu.usta.groccy.dto.sale;

import edu.usta.groccy.enums.SaleStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record SaleResponse(
        Long id,
        LocalDateTime saleDate,
        BigDecimal totalAmount,
        SaleStatus status,
        Long storeId,
        String storeName,
        Long sellerId,
        String sellerName,
        List<SaleDetailResponse> details
) {
}