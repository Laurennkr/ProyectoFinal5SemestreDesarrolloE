package edu.usta.groccy.dto.productionsheet;

import java.math.BigDecimal;

public record RequiredMaterialResponse(
        Long id,
        Long supplyId,
        String supplyName,
        String supplyUnit,
        BigDecimal requiredQuantity,
        String notes
) {}
