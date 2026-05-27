package edu.usta.groccy.controller;

import edu.usta.groccy.dto.stock.*;
import edu.usta.groccy.service.StockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    // ── Stock Central ──────────────────────────────────────
    @PostMapping("/stock-central/entrada")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CentralStockResponse> addToCentralStock(
            @Valid @RequestBody EntryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(stockService.addToCentralStock(request));
    }

    @GetMapping("/stock-central")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CentralStockResponse>> findAllCentralStock() {
        return ResponseEntity.ok(stockService.findAllCentralStock());
    }

    @GetMapping("/stock-central/producto/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CentralStockResponse> findCentralStockByProduct(
            @PathVariable Long productId) {
        return ResponseEntity.ok(stockService.findCentralStockByProduct(productId));
    }

    // ── Distribución ───────────────────────────────────────
    @PostMapping("/distribuciones")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StoreStockResponse> distribute(
            @Valid @RequestBody DistributionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(stockService.distributeToStore(request));
    }

    // ── Stock por Local ────────────────────────────────────
    @GetMapping("/stock-locales/{storeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    public ResponseEntity<List<StoreStockResponse>> findStockByStore(
            @PathVariable Long storeId) {
        return ResponseEntity.ok(stockService.findStockByStore(storeId));
    }

    @GetMapping("/stock-locales/{storeId}/producto/{productId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    public ResponseEntity<StoreStockResponse> findStockByProductAndStore(
            @PathVariable Long storeId,
            @PathVariable Long productId) {
        return ResponseEntity.ok(
                stockService.findStockByProductAndStore(productId, storeId));
    }

    // ── Movimientos ────────────────────────────────────────
    @GetMapping("/movimientos/producto/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<InventoryMovementResponse>> findMovementsByProduct(
            @PathVariable Long productId) {
        return ResponseEntity.ok(stockService.findMovementsByProduct(productId));
    }

    @GetMapping("/movimientos/local/{storeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<InventoryMovementResponse>> findMovementsByStore(
            @PathVariable Long storeId) {
        return ResponseEntity.ok(stockService.findMovementsByStore(storeId));
    }
}