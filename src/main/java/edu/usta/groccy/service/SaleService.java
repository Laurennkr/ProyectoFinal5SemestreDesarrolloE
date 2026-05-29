package edu.usta.groccy.service;

import edu.usta.groccy.dto.sale.SaleRequest;
import edu.usta.groccy.dto.sale.SaleResponse;

import java.util.List;

public interface SaleService {

    SaleResponse create(SaleRequest request);

    List<SaleResponse> findAll();

    SaleResponse findById(Long id);
}