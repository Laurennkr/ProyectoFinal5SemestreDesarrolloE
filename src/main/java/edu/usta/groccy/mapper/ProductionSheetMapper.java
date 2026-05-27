package edu.usta.groccy.mapper;

import edu.usta.groccy.dto.productionsheet.RequiredMaterialResponse;
import edu.usta.groccy.dto.productionsheet.ProductionSheetResponse;
import edu.usta.groccy.entity.ProductionSheet;
import edu.usta.groccy.entity.RequiredMaterial;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductionSheetMapper {

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "assignedTailor.id", target = "assignedTailorId")
    @Mapping(source = "assignedTailor.fullName", target = "assignedTailorName")
    ProductionSheetResponse toResponse(ProductionSheet sheet);

    List<ProductionSheetResponse> toResponseList(List<ProductionSheet> sheets);

    @Mapping(source = "supply.id", target = "supplyId")
    @Mapping(source = "supply.name", target = "supplyName")
    @Mapping(source = "supply.unit", target = "supplyUnit")
    RequiredMaterialResponse toMaterialResponse(RequiredMaterial material);
}