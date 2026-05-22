package edu.usta.groccy.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank(message = "La referencia del producto es obligatoria")
        String reference,

        @NotBlank(message = "El nombre del producto es obligatorio")
        String name,

        @NotBlank(message = "La talla es obligatoria")
        String size,

        @NotBlank(message = "El color es obligatorio")
        String color,

        String category,

        @NotNull(message = "El precio de venta es obligatorio")
        @Positive(message = "El precio de venta debe ser mayor a cero")
        BigDecimal salePrice,

        @NotNull(message = "El costo de producción es obligatorio")
        @Positive(message = "El costo de producción debe ser mayor a cero")
        BigDecimal productionCost
) {
}