package edu.usta.groccy.controller;

import edu.usta.groccy.dto.productionsheet.ProductionSheetRequest;
import edu.usta.groccy.dto.productionsheet.ProductionSheetResponse;
import edu.usta.groccy.service.ProductionSheetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fichas-produccion")
@RequiredArgsConstructor
public class ProductionSheetController {

    private final ProductionSheetService sheetService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductionSheetResponse> create(
            @Valid @RequestBody ProductionSheetRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(sheetService.create(request));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProductionSheetResponse>> findAll() {
        return ResponseEntity.ok(sheetService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TAILOR')")
    public ResponseEntity<ProductionSheetResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(sheetService.findById(id));
    }

    // El costurero consulta solo sus fichas
    @GetMapping("/mis-fichas/{tailorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TAILOR')")
    public ResponseEntity<List<ProductionSheetResponse>> findMySheets(
            @PathVariable Long tailorId) {
        return ResponseEntity.ok(sheetService.findMySheets(tailorId));
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('ADMIN', 'TAILOR')")
    public ResponseEntity<ProductionSheetResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(sheetService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}/cancelar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        sheetService.cancel(id);
        return ResponseEntity.noContent().build();
    }
}