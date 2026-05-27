package edu.usta.groccy.service;

import edu.usta.groccy.dto.productionsheet.ProductionSheetRequest;
import edu.usta.groccy.dto.productionsheet.ProductionSheetResponse;

import java.util.List;

public interface ProductionSheetService {

    ProductionSheetResponse create(ProductionSheetRequest request);

    List<ProductionSheetResponse> findAll();

    ProductionSheetResponse findById(Long id);

    // Para el costurero: solo sus fichas
    List<ProductionSheetResponse> findMySheets(Long tailorId);

    ProductionSheetResponse updateStatus(Long id, String newStatus);

    void cancel(Long id);
}