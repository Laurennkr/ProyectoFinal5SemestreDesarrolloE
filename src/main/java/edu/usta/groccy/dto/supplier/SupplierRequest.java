package edu.usta.groccy.dto.supplier;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SupplierRequest(
        @NotBlank(message = "El nombre del proveedor es obligatorio")
        String name,
        String phone,

        @Email(message = "El correo no tiene un formato válido")
        String email,
        String address
) {
}