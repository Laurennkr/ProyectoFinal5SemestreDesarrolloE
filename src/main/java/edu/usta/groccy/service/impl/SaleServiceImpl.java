package edu.usta.groccy.service.impl;

import edu.usta.groccy.dto.sale.SaleDetailRequest;
import edu.usta.groccy.dto.sale.SaleRequest;
import edu.usta.groccy.dto.sale.SaleResponse;
import edu.usta.groccy.entity.*;
import edu.usta.groccy.enums.MovementType;
import edu.usta.groccy.enums.SaleStatus;
import edu.usta.groccy.enums.Status;
import edu.usta.groccy.exception.BusinessException;
import edu.usta.groccy.exception.ResourceNotFoundException;
import edu.usta.groccy.mapper.SaleMapper;
import edu.usta.groccy.repository.*;
import edu.usta.groccy.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final StoreRepository storeRepository;
    private final StoreStockRepository storeStockRepository;
    private final InventoryMovementRepository inventoryMovementRepository;
    private final UserRepository userRepository;
    private final SaleMapper saleMapper;

    @Override
    @Transactional
    public SaleResponse create(SaleRequest request) {
        Store store = storeRepository.findById(request.storeId())
                .orElseThrow(() -> new ResourceNotFoundException("Local no encontrado con id: " + request.storeId()));

        User seller = getAuthenticatedSeller();

        Map<Long, BigDecimal> quantitiesByProduct = groupQuantitiesByProduct(request.items());

        Sale sale = Sale.builder()
                .saleDate(LocalDateTime.now())
                .totalAmount(BigDecimal.ZERO)
                .status(SaleStatus.COMPLETED)
                .store(store)
                .seller(seller)
                .build();

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (Map.Entry<Long, BigDecimal> entry : quantitiesByProduct.entrySet()) {
            Long productId = entry.getKey();
            BigDecimal quantity = entry.getValue();

            StoreStock stock = storeStockRepository.findByProductIdAndStoreId(productId, store.getId())
                    .orElseThrow(() -> new BusinessException(
                            "No existe stock local para el producto con id: " + productId
                    ));

            validateAvailableStock(stock, quantity);

            Product product = stock.getProduct();
            BigDecimal unitPrice = product.getSalePrice();
            BigDecimal subtotal = unitPrice.multiply(quantity);

            stock.setAvailableQuantity(stock.getAvailableQuantity().subtract(quantity));
            stock.setLastUpdated(LocalDateTime.now());
            storeStockRepository.save(stock);

            SaleDetail detail = SaleDetail.builder()
                    .sale(sale)
                    .product(product)
                    .quantity(quantity)
                    .unitPrice(unitPrice)
                    .subtotal(subtotal)
                    .build();

            sale.getDetails().add(detail);

            InventoryMovement movement = InventoryMovement.builder()
                    .movementType(MovementType.SALE)
                    .quantity(quantity)
                    .movementDate(LocalDateTime.now())
                    .notes("Venta registrada en el local: " + store.getName())
                    .product(product)
                    .store(store)
                    .build();

            inventoryMovementRepository.save(movement);

            totalAmount = totalAmount.add(subtotal);
        }

        sale.setTotalAmount(totalAmount);

        Sale savedSale = saleRepository.save(sale);

        return saleMapper.toResponse(savedSale);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SaleResponse> findAll() {
        return saleMapper.toResponseList(
                saleRepository.findAllByStatus(SaleStatus.COMPLETED)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public SaleResponse findById(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con id: " + id));

        return saleMapper.toResponse(sale);
    }

    private User getAuthenticatedSeller() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmailAndStatus(email, Status.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario autenticado no encontrado"));
    }

    private Map<Long, BigDecimal> groupQuantitiesByProduct(List<SaleDetailRequest> items) {
        Map<Long, BigDecimal> quantitiesByProduct = new LinkedHashMap<>();

        for (SaleDetailRequest item : items) {
            if (item.quantity().compareTo(BigDecimal.ZERO) <= 0) {
                throw new BusinessException("La cantidad vendida debe ser mayor a cero");
            }

            quantitiesByProduct.merge(
                    item.productId(),
                    item.quantity(),
                    BigDecimal::add
            );
        }

        return quantitiesByProduct;
    }

    private void validateAvailableStock(StoreStock stock, BigDecimal quantity) {
        if (stock.getAvailableQuantity().compareTo(quantity) < 0) {
            throw new BusinessException(
                    "Stock insuficiente para el producto: " + stock.getProduct().getName()
            );
        }
    }
}