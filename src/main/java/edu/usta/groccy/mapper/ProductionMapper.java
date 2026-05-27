package edu.usta.groccy.mapper;

import edu.usta.groccy.dto.production.ProductionReportResponse;
import edu.usta.groccy.entity.Production;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductionMapper {

    @Mapping(source = "productionSheet.id", target = "productionSheetId")
    @Mapping(source = "productionSheet.title", target = "productionSheetTitle")
    @Mapping(source = "tailor.id", target = "tailorId")
    @Mapping(source = "tailor.fullName", target = "tailorName")
    ProductionReportResponse toResponse(Production production);

    List<ProductionReportResponse> toResponseList(List<Production> productions);
}