package edu.usta.groccy.service.impl;

import edu.usta.groccy.dto.supply.SupplyRequest;
import edu.usta.groccy.dto.supply.SupplyResponse;
import edu.usta.groccy.entity.Supply;
import edu.usta.groccy.entity.Supplier;
import edu.usta.groccy.enums.Status;
import edu.usta.groccy.exception.BusinessException;
import edu.usta.groccy.exception.ResourceNotFoundException;
import edu.usta.groccy.mapper.SupplyMapper;
import edu.usta.groccy.repository.SupplyRepository;
import edu.usta.groccy.repository.SupplierRepository;
import edu.usta.groccy.service.SupplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplyServiceImpl implements SupplyService {

    private final SupplyRepository supplyRepository;
    private final SupplierRepository supplierRepository;
    private final SupplyMapper supplyMapper;

    @Override
    public SupplyResponse create(SupplyRequest request) {
        Supplier supplier = supplierRepository.findById(request.supplierId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Proveedor no encontrado con ID: " + request.supplierId()));

        if (supplyRepository.existsByNameAndSupplierId(request.name(), request.supplierId())) {
            throw new BusinessException(
                    "Ya existe un insumo con ese nombre para este proveedor");
        }

        Supply supply = Supply.builder()
                .name(request.name())
                .unit(request.unit())
                .availableQuantity(request.availableQuantity())
                .minimumStock(request.minimumStock())
                .description(request.description())
                .status(Status.ACTIVE)
                .supplier(supplier)
                .build();

        return supplyMapper.toResponse(supplyRepository.save(supply));
    }

    @Override
    public List<SupplyResponse> findAll() {
        return supplyMapper.toResponseList(
                supplyRepository.findAllByStatus(Status.ACTIVE));
    }

    @Override
    public SupplyResponse findById(Long id) {
        Supply supply = supplyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Insumo no encontrado con ID: " + id));
        return supplyMapper.toResponse(supply);
    }

    @Override
    public SupplyResponse update(Long id, SupplyRequest request) {
        Supply supply = supplyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Insumo no encontrado con ID: " + id));

        Supplier supplier = supplierRepository.findById(request.supplierId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Proveedor no encontrado con ID: " + request.supplierId()));

        supply.setName(request.name());
        supply.setUnit(request.unit());
        supply.setAvailableQuantity(request.availableQuantity());
        supply.setMinimumStock(request.minimumStock());
        supply.setDescription(request.description());
        supply.setSupplier(supplier);

        return supplyMapper.toResponse(supplyRepository.save(supply));
    }

    @Override
    public void delete(Long id) {
        Supply supply = supplyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Insumo no encontrado con ID: " + id));
        supply.setStatus(Status.INACTIVE);
        supplyRepository.save(supply);
    }
}