package edu.usta.groccy.dto.shortage;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record SupplyShortageRequest(

        @NotNull(message = "El ID de la ficha de producción es obligatorio")
        Long productionSheetId,

        @NotNull(message = "El ID del insumo es obligatorio")
        Long supplyId,

        @NotNull(message = "La cantidad faltante es obligatoria")
        @Positive(message = "La cantidad debe ser positiva")
        BigDecimal missingQuantity,

        @NotBlank(message = "El motivo del faltante es obligatorio")
        String reason
) {}