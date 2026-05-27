package edu.usta.groccy.dto.productionsheet;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record RequiredMaterialRequest(

        @NotNull(message = "El ID del insumo es obligatorio")
        Long supplyId,

        @NotNull(message = "La cantidad requerida es obligatoria")
        @Positive(message = "La cantidad debe ser positiva")
        BigDecimal requiredQuantity,

        String notes
) {}