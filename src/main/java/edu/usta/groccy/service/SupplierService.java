package edu.usta.groccy.service;

import edu.usta.groccy.dto.supplier.SupplierRequest;
import edu.usta.groccy.dto.supplier.SupplierResponse;

import java.util.List;

public interface SupplierService {

    SupplierResponse create(SupplierRequest request);
    List<SupplierResponse> findAll();
    SupplierResponse findById(Long id);
    SupplierResponse update(Long id, SupplierRequest request);
    void delete(Long id);
}