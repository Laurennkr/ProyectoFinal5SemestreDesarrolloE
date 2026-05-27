package edu.usta.groccy.service.impl;

import edu.usta.groccy.dto.productionsheet.ProductionSheetRequest;
import edu.usta.groccy.dto.productionsheet.ProductionSheetResponse;
import edu.usta.groccy.entity.Product;
import edu.usta.groccy.entity.ProductionSheet;
import edu.usta.groccy.entity.RequiredMaterial;
import edu.usta.groccy.entity.Supply;
import edu.usta.groccy.entity.User;
import edu.usta.groccy.enums.ProductionSheetStatus;
import edu.usta.groccy.enums.Role;
import edu.usta.groccy.exception.BusinessException;
import edu.usta.groccy.exception.ResourceNotFoundException;
import edu.usta.groccy.mapper.ProductionSheetMapper;
import edu.usta.groccy.repository.ProductionSheetRepository;
import edu.usta.groccy.repository.ProductRepository;
import edu.usta.groccy.repository.SupplyRepository;
import edu.usta.groccy.repository.UserRepository;
import edu.usta.groccy.enums.Status;
import edu.usta.groccy.service.ProductionSheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductionSheetServiceImpl implements ProductionSheetService {

    private final ProductionSheetRepository sheetRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final SupplyRepository supplyRepository;
    private final ProductionSheetMapper sheetMapper;

    @Override
    @Transactional
    public ProductionSheetResponse create(ProductionSheetRequest request) {

        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Producto no encontrado con ID: " + request.productId()));

        User tailor = userRepository.findById(request.assignedTailorId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con ID: " + request.assignedTailorId()));

        if (!tailor.getRole().equals(Role.TAILOR)) {
            throw new BusinessException(
                    "El usuario asignado no tiene rol de costurero");
        }

        ProductionSheet sheet = ProductionSheet.builder()
                .title(request.title())
                .requestedQuantity(request.requestedQuantity())
                .referenceImageUrl(request.referenceImageUrl())
                .cuttingGuide(request.cuttingGuide())
                .deadline(request.deadline())
                .status(ProductionSheetStatus.PENDING)
                .product(product)
                .assignedTailor(tailor)
                .requiredMaterials(new ArrayList<>())
                .build();

        if (request.requiredMaterials() != null) {
            for (var materialReq : request.requiredMaterials()) {
                Supply supply = supplyRepository.findById(materialReq.supplyId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Insumo no encontrado con ID: " + materialReq.supplyId()));

                RequiredMaterial material = RequiredMaterial.builder()
                        .requiredQuantity(materialReq.requiredQuantity())
                        .notes(materialReq.notes())
                        .productionSheet(sheet)
                        .supply(supply)
                        .build();

                sheet.getRequiredMaterials().add(material);
            }
        }

        return sheetMapper.toResponse(sheetRepository.save(sheet));
    }

    @Override
    public List<ProductionSheetResponse> findAll() {
        return sheetMapper.toResponseList(sheetRepository.findAll());
    }

    @Override
    public ProductionSheetResponse findById(Long id) {
        ProductionSheet sheet = sheetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Ficha de producción no encontrada con ID: " + id));
        return sheetMapper.toResponse(sheet);
    }

    @Override
    public List<ProductionSheetResponse> findMySheets(Long tailorId) {
        return sheetMapper.toResponseList(
                sheetRepository.findAllByAssignedTailorId(tailorId));
    }

    @Override
    @Transactional
    public ProductionSheetResponse updateStatus(Long id, String newStatus) {
        ProductionSheet sheet = sheetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Ficha de producción no encontrada con ID: " + id));

        ProductionSheetStatus status;
        try {
            status = ProductionSheetStatus.valueOf(newStatus.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Estado inválido: " + newStatus);
        }

        sheet.setStatus(status);
        return sheetMapper.toResponse(sheetRepository.save(sheet));
    }

    @Override
    @Transactional
    public void cancel(Long id) {
        ProductionSheet sheet = sheetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Ficha de producción no encontrada con ID: " + id));
        sheet.setStatus(ProductionSheetStatus.CANCELLED);
        sheetRepository.save(sheet);
    }
}