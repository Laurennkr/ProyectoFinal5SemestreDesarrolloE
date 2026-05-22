package edu.usta.groccy.mapper;

import edu.usta.groccy.dto.supplier.SupplierRequest;
import edu.usta.groccy.dto.supplier.SupplierResponse;
import edu.usta.groccy.entity.Supplier;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SupplierMapper {

    Supplier toEntity(SupplierRequest request);
    SupplierResponse toResponse(Supplier supplier);
    List<SupplierResponse> toResponseList(List<Supplier> suppliers);
}