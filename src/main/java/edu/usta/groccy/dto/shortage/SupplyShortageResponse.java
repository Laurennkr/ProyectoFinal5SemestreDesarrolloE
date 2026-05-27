package edu.usta.groccy.dto.shortage;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SupplyShortageResponse(
        Long id,
        BigDecimal missingQuantity,
        String reason,
        LocalDateTime reportedAt,
        Long productionSheetId,
        String productionSheetTitle,
        Long supplyId,
        String supplyName,
        Long tailorId,
        String tailorName
) {}