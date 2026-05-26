package edu.usta.groccy.mapper;

import edu.usta.groccy.dto.supply.SupplyResponse;
import edu.usta.groccy.entity.Supply;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SupplyMapper {

    @Mapping(source = "supplier.id", target = "supplierId")
    @Mapping(source = "supplier.name", target = "supplierName")
    SupplyResponse toResponse(Supply supply);

    List<SupplyResponse> toResponseList(List<Supply> supplies);
}