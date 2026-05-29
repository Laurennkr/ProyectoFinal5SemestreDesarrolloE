package edu.usta.groccy.controller;

import edu.usta.groccy.dto.sale.SaleRequest;
import edu.usta.groccy.dto.sale.SaleResponse;
import edu.usta.groccy.service.SaleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ventas")
@RequiredArgsConstructor
public class SaleController {

    private final SaleService saleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SaleResponse create(@Valid @RequestBody SaleRequest request) {
        return saleService.create(request);
    }

    @GetMapping
    public List<SaleResponse> findAll() {
        return saleService.findAll();
    }

    @GetMapping("/{id}")
    public SaleResponse findById(@PathVariable Long id) {
        return saleService.findById(id);
    }
}