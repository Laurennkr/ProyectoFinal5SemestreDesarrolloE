package edu.usta.groccy.mapper;

import edu.usta.groccy.dto.sale.SaleDetailResponse;
import edu.usta.groccy.dto.sale.SaleResponse;
import edu.usta.groccy.entity.Sale;
import edu.usta.groccy.entity.SaleDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SaleMapper {

    @Mapping(source = "store.id", target = "storeId")
    @Mapping(source = "store.name", target = "storeName")
    @Mapping(source = "seller.id", target = "sellerId")
    @Mapping(source = "seller.fullName", target = "sellerName")
    @Mapping(source = "details", target = "details")
    SaleResponse toResponse(Sale sale);

    List<SaleResponse> toResponseList(List<Sale> sales);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.reference", target = "productReference")
    @Mapping(source = "product.name", target = "productName")
    SaleDetailResponse toDetailResponse(SaleDetail detail);

    List<SaleDetailResponse> toDetailResponseList(List<SaleDetail> details);
}