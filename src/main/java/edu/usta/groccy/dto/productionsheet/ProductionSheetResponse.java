package edu.usta.groccy.dto.productionsheet;

import edu.usta.groccy.enums.ProductionSheetStatus;

import java.time.LocalDate;
import java.util.List;

public record ProductionSheetResponse(
        Long id,
        String title,
        Integer requestedQuantity,
        String referenceImageUrl,
        String cuttingGuide,
        LocalDate deadline,
        ProductionSheetStatus status,
        Long productId,
        String productName,
        Long assignedTailorId,
        String assignedTailorName,
        List<RequiredMaterialResponse> requiredMaterials
) {}