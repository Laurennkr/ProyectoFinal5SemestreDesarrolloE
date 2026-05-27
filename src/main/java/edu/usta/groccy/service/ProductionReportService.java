package edu.usta.groccy.service;

import edu.usta.groccy.dto.production.ProductionReportRequest;
import edu.usta.groccy.dto.production.ProductionReportResponse;
import edu.usta.groccy.dto.shortage.SupplyShortageRequest;
import edu.usta.groccy.dto.shortage.SupplyShortageResponse;

import java.util.List;

public interface ProductionReportService {

    ProductionReportResponse reportCompleted(Long tailorId, ProductionReportRequest request);

    SupplyShortageResponse reportShortage(Long tailorId, SupplyShortageRequest request);

    List<ProductionReportResponse> findReportsBySheet(Long productionSheetId);

    List<SupplyShortageResponse> findShortagesBySheet(Long productionSheetId);
}