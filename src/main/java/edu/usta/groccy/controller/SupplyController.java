package edu.usta.groccy.controller;

import edu.usta.groccy.dto.supply.SupplyRequest;
import edu.usta.groccy.dto.supply.SupplyResponse;
import edu.usta.groccy.service.SupplyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/insumos")
@RequiredArgsConstructor
public class SupplyController {

    private final SupplyService supplyService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SupplyResponse> create(@Valid @RequestBody SupplyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(supplyService.create(request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER', 'TAILOR')")
    public ResponseEntity<List<SupplyResponse>> findAll() {
        return ResponseEntity.ok(supplyService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER', 'TAILOR')")
    public ResponseEntity<SupplyResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(supplyService.findById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SupplyResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody SupplyRequest request) {
        return ResponseEntity.ok(supplyService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        supplyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}