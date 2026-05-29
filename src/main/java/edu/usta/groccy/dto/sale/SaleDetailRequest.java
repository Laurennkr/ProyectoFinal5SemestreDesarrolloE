package edu.usta.groccy.dto.sale;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record SaleDetailRequest(

        @NotNull(message = "El producto es obligatorio")
        Long productId,

        @NotNull(message = "La cantidad es obligatoria")
        @Positive(message = "La cantidad debe ser mayor a cero")
        BigDecimal quantity
) {
}