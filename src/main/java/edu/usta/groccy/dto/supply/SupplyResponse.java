package edu.usta.groccy.dto.supply;

import edu.usta.groccy.enums.Status;

import java.math.BigDecimal;

public record SupplyResponse(
        Long id,
        String name,
        String unit,
        BigDecimal availableQuantity,
        BigDecimal minimumStock,
        String description,
        Status status,
        Long supplierId,
        String supplierName
) {}