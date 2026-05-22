package edu.usta.groccy.service;

import edu.usta.groccy.dto.product.ProductRequest;
import edu.usta.groccy.dto.product.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse create(ProductRequest request);
    List<ProductResponse> findAll();
    ProductResponse findById(Long id);
    ProductResponse update(Long id, ProductRequest request);
    void delete(Long id);
}