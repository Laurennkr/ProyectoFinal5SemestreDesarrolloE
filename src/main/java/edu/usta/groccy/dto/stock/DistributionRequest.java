package edu.usta.groccy.dto.stock;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record DistributionRequest(

        @NotNull(message = "El ID del producto es obligatorio")
        Long productId,

        @NotNull(message = "El ID del local es obligatorio")
        Long storeId,

        @NotNull(message = "La cantidad a distribuir es obligatoria")
        @Positive(message = "La cantidad debe ser positiva")
        BigDecimal quantity,

        String notes
) {}