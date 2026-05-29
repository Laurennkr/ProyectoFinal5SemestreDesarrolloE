package edu.usta.groccy.dto.sale;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SaleRequest(

        @NotNull(message = "El local es obligatorio")
        Long storeId,

        @NotEmpty(message = "La venta debe tener al menos un producto")
        List<@Valid SaleDetailRequest> items
) {
}