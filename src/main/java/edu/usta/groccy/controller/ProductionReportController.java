package edu.usta.groccy.controller;

import edu.usta.groccy.dto.production.ProductionReportRequest;
import edu.usta.groccy.dto.production.ProductionReportResponse;
import edu.usta.groccy.dto.shortage.SupplyShortageRequest;
import edu.usta.groccy.dto.shortage.SupplyShortageResponse;
import edu.usta.groccy.service.ProductionReportService;
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
public class ProductionReportController {

    private final ProductionReportService reportService;

    // Costurero reporta unidades realizadas
    @PostMapping("/producciones/{tailorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TAILOR')")
    public ResponseEntity<ProductionReportResponse> reportCompleted(
            @PathVariable Long tailorId,
            @Valid @RequestBody ProductionReportRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reportService.reportCompleted(tailorId, request));
    }

    // Costurero reporta faltante de insumo
    @PostMapping("/faltantes/{tailorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TAILOR')")
    public ResponseEntity<SupplyShortageResponse> reportShortage(
            @PathVariable Long tailorId,
            @Valid @RequestBody SupplyShortageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reportService.reportShortage(tailorId, request));
    }

    // Admin consulta reportes de una ficha
    @GetMapping("/producciones/ficha/{sheetId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProductionReportResponse>> findReportsBySheet(
            @PathVariable Long sheetId) {
        return ResponseEntity.ok(reportService.findReportsBySheet(sheetId));
    }

    // Admin consulta faltantes de una ficha
    @GetMapping("/faltantes/ficha/{sheetId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SupplyShortageResponse>> findShortagesBySheet(
            @PathVariable Long sheetId) {
        return ResponseEntity.ok(reportService.findShortagesBySheet(sheetId));
    }
}