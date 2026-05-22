package edu.usta.groccy.mapper;

import edu.usta.groccy.dto.product.ProductRequest;
import edu.usta.groccy.dto.product.ProductResponse;
import edu.usta.groccy.entity.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity(ProductRequest request);
    ProductResponse toResponse(Product product);
    List<ProductResponse> toResponseList(List<Product> products);
}