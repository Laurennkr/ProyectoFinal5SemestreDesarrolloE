package edu.usta.groccy.mapper;

import edu.usta.groccy.dto.stock.CentralStockResponse;
import edu.usta.groccy.entity.CentralStock;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CentralStockMapper {

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.reference", target = "productReference")
    CentralStockResponse toResponse(CentralStock centralStock);

    List<CentralStockResponse> toResponseList(List<CentralStock> list);
}