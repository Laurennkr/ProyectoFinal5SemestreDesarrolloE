package edu.usta.groccy.mapper;

import edu.usta.groccy.dto.stock.InventoryMovementResponse;
import edu.usta.groccy.entity.InventoryMovement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InventoryMovementMapper {

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "store.id", target = "storeId")
    @Mapping(source = "store.name", target = "storeName")
    InventoryMovementResponse toResponse(InventoryMovement movement);

    List<InventoryMovementResponse> toResponseList(List<InventoryMovement> list);
}