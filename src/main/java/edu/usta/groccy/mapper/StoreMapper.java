package edu.usta.groccy.mapper;

import edu.usta.groccy.dto.store.StoreRequest;
import edu.usta.groccy.dto.store.StoreResponse;
import edu.usta.groccy.entity.Store;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StoreMapper {

    Store toEntity(StoreRequest request);
    StoreResponse toResponse(Store store);
    List<StoreResponse> toResponseList(List<Store> stores);
}