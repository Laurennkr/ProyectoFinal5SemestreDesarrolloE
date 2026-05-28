package edu.usta.groccy.service.impl;

import edu.usta.groccy.dto.product.ProductRequest;
import edu.usta.groccy.dto.product.ProductResponse;
import edu.usta.groccy.entity.Product;
import edu.usta.groccy.enums.Status;
import edu.usta.groccy.exception.BusinessException;
import edu.usta.groccy.exception.ResourceNotFoundException;
import edu.usta.groccy.mapper.ProductMapper;
import edu.usta.groccy.repository.ProductRepository;
import edu.usta.groccy.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import edu.usta.groccy.enums.Status;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponse create(ProductRequest request) {
        if (productRepository.existsByReference(request.reference())) {
            throw new BusinessException("Ya existe un producto con esa referencia");
        }

        Product product = productMapper.toEntity(request);
        product.setStatus(Status.ACTIVE);

        return productMapper.toResponse(productRepository.save(product));
    }

    @Override
    public List<ProductResponse> findAll() {
        return productMapper.toResponseList(
                productRepository.findAllByStatus(Status.ACTIVE)
        );
    }

    @Override
    public ProductResponse findById(Long id) {
        Product product = findProductById(id);
        return productMapper.toResponse(product);
    }

    @Override
    public ProductResponse update(Long id, ProductRequest request) {
        Product product = findProductById(id);

        product.setReference(request.reference());
        product.setName(request.name());
        product.setSize(request.size());
        product.setColor(request.color());
        product.setCategory(request.category());
        product.setSalePrice(request.salePrice());
        product.setProductionCost(request.productionCost());

        return productMapper.toResponse(productRepository.save(product));
    }

    @Override
    public void delete(Long id) {
        Product product = findProductById(id);
        product.setStatus(Status.INACTIVE);
        productRepository.save(product);
    }

    private Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
    }
}