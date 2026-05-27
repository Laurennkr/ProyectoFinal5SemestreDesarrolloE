package edu.usta.groccy.dto.supplier;

import edu.usta.groccy.enums.Status;

public record SupplierResponse(
        Long id,
        String name,
        String phone,
        String email,
        String address,
        Status status
) {
}


//Para proveedores