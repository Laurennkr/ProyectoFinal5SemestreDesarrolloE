package edu.usta.groccy.dto.store;

import edu.usta.groccy.enums.Status;

public record StoreResponse(
        Long id,
        String name,
        String address,
        String zone,
        String type,
        Status status
) {
}