package edu.usta.groccy.mapper;

import edu.usta.groccy.dto.stock.StoreStockResponse;
import edu.usta.groccy.entity.StoreStock;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StoreStockMapper {

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.reference", target = "productReference")
    @Mapping(source = "store.id", target = "storeId")
    @Mapping(source = "store.name", target = "storeName")
    StoreStockResponse toResponse(StoreStock storeStock);

    List<StoreStockResponse> toResponseList(List<StoreStock> list);
}