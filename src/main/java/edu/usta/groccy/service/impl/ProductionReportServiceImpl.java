package edu.usta.groccy.service.impl;

import edu.usta.groccy.dto.production.ProductionReportRequest;
import edu.usta.groccy.dto.production.ProductionReportResponse;
import edu.usta.groccy.dto.shortage.SupplyShortageRequest;
import edu.usta.groccy.dto.shortage.SupplyShortageResponse;
import edu.usta.groccy.entity.*;
import edu.usta.groccy.enums.AlertStatus;
import edu.usta.groccy.enums.AlertType;
import edu.usta.groccy.enums.ProductionSheetStatus;
import edu.usta.groccy.exception.BusinessException;
import edu.usta.groccy.exception.ResourceNotFoundException;
import edu.usta.groccy.mapper.ProductionMapper;
import edu.usta.groccy.mapper.SupplyShortageMapper;
import edu.usta.groccy.repository.*;
import edu.usta.groccy.service.ProductionReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductionReportServiceImpl implements ProductionReportService {

    private final ProductionRepository productionRepository;
    private final SupplyShortageRepository shortageRepository;
    private final ProductionSheetRepository sheetRepository;
    private final SupplyRepository supplyRepository;
    private final UserRepository userRepository;
    private final AlertRepository alertRepository;
    private final ProductionMapper productionMapper;
    private final SupplyShortageMapper shortageMapper;

    @Override
    @Transactional
    public ProductionReportResponse reportCompleted(Long tailorId,
                                                    ProductionReportRequest request) {
        User tailor = userRepository.findById(tailorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Costurero no encontrado con ID: " + tailorId));

        ProductionSheet sheet = sheetRepository.findById(request.productionSheetId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Ficha de producción no encontrada con ID: "
                                + request.productionSheetId()));

        if (sheet.getStatus() == ProductionSheetStatus.CANCELLED
                || sheet.getStatus() == ProductionSheetStatus.COMPLETED) {
            throw new BusinessException(
                    "No se puede reportar producción en una ficha " +
                            sheet.getStatus().name().toLowerCase());
        }

        // Actualizar estado de la ficha a IN_PRODUCTION si estaba PENDING
        if (sheet.getStatus() == ProductionSheetStatus.PENDING) {
            sheet.setStatus(ProductionSheetStatus.IN_PRODUCTION);
            sheetRepository.save(sheet);
        }

        Production production = Production.builder()
                .completedQuantity(request.completedQuantity())
                .observations(request.observations())
                .reportedAt(LocalDateTime.now())
                .productionSheet(sheet)
                .tailor(tailor)
                .build();

        Production saved = productionRepository.save(production);

        // Crear alerta de producción completada para el admin
        Alert alert = Alert.builder()
                .type(AlertType.PRODUCTION_COMPLETED)
                .message("El costurero " + tailor.getFullName()
                        + " reportó " + request.completedQuantity()
                        + " unidades en la ficha: " + sheet.getTitle())
                .status(AlertStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .relatedEntityId(sheet.getId())
                .relatedEntityType("ProductionSheet")
                .build();

        alertRepository.save(alert);

        return productionMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public SupplyShortageResponse reportShortage(Long tailorId,
                                                 SupplyShortageRequest request) {
        User tailor = userRepository.findById(tailorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Costurero no encontrado con ID: " + tailorId));

        ProductionSheet sheet = sheetRepository.findById(request.productionSheetId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Ficha de producción no encontrada con ID: "
                                + request.productionSheetId()));

        Supply supply = supplyRepository.findById(request.supplyId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Insumo no encontrado con ID: " + request.supplyId()));

        SupplyShortage shortage = SupplyShortage.builder()
                .missingQuantity(request.missingQuantity())
                .reason(request.reason())
                .reportedAt(LocalDateTime.now())
                .productionSheet(sheet)
                .supply(supply)
                .tailor(tailor)
                .build();

        SupplyShortage saved = shortageRepository.save(shortage);

        // Crear alerta de faltante para el admin
        Alert alert = Alert.builder()
                .type(AlertType.SUPPLY_SHORTAGE)
                .message("Faltante reportado por " + tailor.getFullName()
                        + ": " + request.missingQuantity()
                        + " " + supply.getUnit()
                        + " de " + supply.getName()
                        + " en ficha: " + sheet.getTitle())
                .status(AlertStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .relatedEntityId(sheet.getId())
                .relatedEntityType("ProductionSheet")
                .build();

        alertRepository.save(alert);

        return shortageMapper.toResponse(saved);
    }

    @Override
    public List<ProductionReportResponse> findReportsBySheet(Long productionSheetId) {
        return productionMapper.toResponseList(
                productionRepository.findAllByProductionSheetId(productionSheetId));
    }

    @Override
    public List<SupplyShortageResponse> findShortagesBySheet(Long productionSheetId) {
        return shortageMapper.toResponseList(
                shortageRepository.findAllByProductionSheetId(productionSheetId));
    }
}