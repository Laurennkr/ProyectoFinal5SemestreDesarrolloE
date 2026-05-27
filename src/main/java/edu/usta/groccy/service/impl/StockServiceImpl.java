package edu.usta.groccy.service.impl;

import edu.usta.groccy.dto.stock.*;
import edu.usta.groccy.entity.*;
import edu.usta.groccy.enums.AlertStatus;
import edu.usta.groccy.enums.AlertType;
import edu.usta.groccy.enums.MovementType;
import edu.usta.groccy.exception.BusinessException;
import edu.usta.groccy.exception.ResourceNotFoundException;
import edu.usta.groccy.mapper.*;
import edu.usta.groccy.repository.*;
import edu.usta.groccy.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final CentralStockRepository centralStockRepository;
    private final StoreStockRepository storeStockRepository;
    private final InventoryMovementRepository movementRepository;
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final AlertRepository alertRepository;
    private final CentralStockMapper centralStockMapper;
    private final StoreStockMapper storeStockMapper;
    private final InventoryMovementMapper movementMapper;

    @Override
    @Transactional
    public CentralStockResponse addToCentralStock(EntryRequest request) {
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Producto no encontrado con ID: " + request.productId()));

        CentralStock stock = centralStockRepository
                .findByProductId(request.productId())
                .orElse(CentralStock.builder()
                        .product(product)
                        .availableQuantity(java.math.BigDecimal.ZERO)
                        .lastUpdated(LocalDateTime.now())
                        .build());

        stock.setAvailableQuantity(stock.getAvailableQuantity().add(request.quantity()));
        stock.setLastUpdated(LocalDateTime.now());
        CentralStock saved = centralStockRepository.save(stock);

        // Registrar movimiento
        movementRepository.save(InventoryMovement.builder()
                .movementType(MovementType.ENTRY)
                .quantity(request.quantity())
                .movementDate(LocalDateTime.now())
                .notes(request.notes())
                .product(product)
                .store(null)
                .build());

        return centralStockMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public StoreStockResponse distributeToStore(DistributionRequest request) {
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Producto no encontrado con ID: " + request.productId()));

        Store store = storeRepository.findById(request.storeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Local no encontrado con ID: " + request.storeId()));

        // Validar que hay suficiente stock central
        CentralStock centralStock = centralStockRepository
                .findByProductId(request.productId())
                .orElseThrow(() -> new BusinessException(
                        "No existe stock central para el producto: " + product.getName()));

        if (centralStock.getAvailableQuantity().compareTo(request.quantity()) < 0) {
            throw new BusinessException(
                    "Stock central insuficiente. Disponible: "
                            + centralStock.getAvailableQuantity()
                            + " — Solicitado: " + request.quantity());
        }

        // Descontar del stock central
        centralStock.setAvailableQuantity(
                centralStock.getAvailableQuantity().subtract(request.quantity()));
        centralStock.setLastUpdated(LocalDateTime.now());
        centralStockRepository.save(centralStock);

        // Agregar al stock del local
        StoreStock storeStock = storeStockRepository
                .findByProductIdAndStoreId(request.productId(), request.storeId())
                .orElse(StoreStock.builder()
                        .product(product)
                        .store(store)
                        .availableQuantity(java.math.BigDecimal.ZERO)
                        .minimumStock(new java.math.BigDecimal("5"))
                        .lastUpdated(LocalDateTime.now())
                        .build());

        storeStock.setAvailableQuantity(
                storeStock.getAvailableQuantity().add(request.quantity()));
        storeStock.setLastUpdated(LocalDateTime.now());
        StoreStock saved = storeStockRepository.save(storeStock);

        // Movimiento TRANSFER_OUT en central
        movementRepository.save(InventoryMovement.builder()
                .movementType(MovementType.TRANSFER_OUT)
                .quantity(request.quantity())
                .movementDate(LocalDateTime.now())
                .notes("Distribución a local: " + store.getName()
                        + (request.notes() != null ? " — " + request.notes() : ""))
                .product(product)
                .store(null)
                .build());

        // Movimiento TRANSFER_IN en local
        movementRepository.save(InventoryMovement.builder()
                .movementType(MovementType.TRANSFER_IN)
                .quantity(request.quantity())
                .movementDate(LocalDateTime.now())
                .notes("Recibido desde stock central"
                        + (request.notes() != null ? " — " + request.notes() : ""))
                .product(product)
                .store(store)
                .build());

        return storeStockMapper.toResponse(saved);
    }

    @Override
    public List<CentralStockResponse> findAllCentralStock() {
        return centralStockMapper.toResponseList(centralStockRepository.findAll());
    }

    @Override
    public CentralStockResponse findCentralStockByProduct(Long productId) {
        CentralStock stock = centralStockRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe stock central para el producto ID: " + productId));
        return centralStockMapper.toResponse(stock);
    }

    @Override
    public List<StoreStockResponse> findStockByStore(Long storeId) {
        return storeStockMapper.toResponseList(
                storeStockRepository.findAllByStoreId(storeId));
    }

    @Override
    public StoreStockResponse findStockByProductAndStore(Long productId, Long storeId) {
        StoreStock stock = storeStockRepository
                .findByProductIdAndStoreId(productId, storeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe stock para ese producto en ese local"));
        return storeStockMapper.toResponse(stock);
    }

    @Override
    public List<InventoryMovementResponse> findMovementsByProduct(Long productId) {
        return movementMapper.toResponseList(
                movementRepository.findAllByProductId(productId));
    }

    @Override
    public List<InventoryMovementResponse> findMovementsByStore(Long storeId) {
        return movementMapper.toResponseList(
                movementRepository.findAllByStoreId(storeId));
    }
}