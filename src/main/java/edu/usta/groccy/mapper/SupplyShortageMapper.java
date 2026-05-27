package edu.usta.groccy.mapper;

import edu.usta.groccy.dto.shortage.SupplyShortageResponse;
import edu.usta.groccy.entity.SupplyShortage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SupplyShortageMapper {

    @Mapping(source = "productionSheet.id", target = "productionSheetId")
    @Mapping(source = "productionSheet.title", target = "productionSheetTitle")
    @Mapping(source = "supply.id", target = "supplyId")
    @Mapping(source = "supply.name", target = "supplyName")
    @Mapping(source = "tailor.id", target = "tailorId")
    @Mapping(source = "tailor.fullName", target = "tailorName")
    SupplyShortageResponse toResponse(SupplyShortage shortage);

    List<SupplyShortageResponse> toResponseList(List<SupplyShortage> shortages);
}