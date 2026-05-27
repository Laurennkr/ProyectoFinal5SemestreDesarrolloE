package edu.usta.groccy.service;

import edu.usta.groccy.dto.stock.*;

import java.util.List;

public interface StockService {

    // Stock central
    CentralStockResponse addToCentralStock(EntryRequest request);
    List<CentralStockResponse> findAllCentralStock();
    CentralStockResponse findCentralStockByProduct(Long productId);

    // Distribución central → local
    StoreStockResponse distributeToStore(DistributionRequest request);

    // Stock por local
    List<StoreStockResponse> findStockByStore(Long storeId);
    StoreStockResponse findStockByProductAndStore(Long productId, Long storeId);

    // Movimientos
    List<InventoryMovementResponse> findMovementsByProduct(Long productId);
    List<InventoryMovementResponse> findMovementsByStore(Long storeId);
}