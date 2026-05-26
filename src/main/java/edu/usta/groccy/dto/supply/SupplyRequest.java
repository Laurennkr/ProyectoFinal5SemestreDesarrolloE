package edu.usta.groccy.dto.supply;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record SupplyRequest(

        @NotBlank(message = "El nombre del insumo es obligatorio")
        String name,

        @NotBlank(message = "La unidad de medida es obligatoria")
        String unit,

        @NotNull(message = "La cantidad disponible es obligatoria")
        @Positive(message = "La cantidad debe ser positiva")
        BigDecimal availableQuantity,

        @NotNull(message = "El stock mínimo es obligatorio")
        @Positive(message = "El stock mínimo debe ser positivo")
        BigDecimal minimumStock,

        String description,

        @NotNull(message = "El ID del proveedor es obligatorio")
        Long supplierId
) {}