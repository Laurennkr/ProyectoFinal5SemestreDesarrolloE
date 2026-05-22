package edu.usta.groccy.dto.store;

import jakarta.validation.constraints.NotBlank;

public record StoreRequest(
        @NotBlank(message = "El nombre del local es obligatorio")
        String name,
        String address,
        String zone,
        String type
) {
}