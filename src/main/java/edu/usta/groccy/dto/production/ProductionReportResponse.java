package edu.usta.groccy.dto.production;

import java.time.LocalDateTime;

public record ProductionReportResponse(
        Long id,
        Integer completedQuantity,
        String observations,
        LocalDateTime reportedAt,
        Long productionSheetId,
        String productionSheetTitle,
        Long tailorId,
        String tailorName
) {}