package edu.usta.groccy.service.impl;

import edu.usta.groccy.dto.supplier.SupplierRequest;
import edu.usta.groccy.dto.supplier.SupplierResponse;
import edu.usta.groccy.entity.Supplier;
import edu.usta.groccy.enums.Status;
import edu.usta.groccy.exception.BusinessException;
import edu.usta.groccy.exception.ResourceNotFoundException;
import edu.usta.groccy.mapper.SupplierMapper;
import edu.usta.groccy.repository.SupplierRepository;
import edu.usta.groccy.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    @Override
    public SupplierResponse create(SupplierRequest request) {
        if (request.email() != null && supplierRepository.existsByEmail(request.email())) {
            throw new BusinessException("Ya existe un proveedor con ese correo");
        }

        Supplier supplier = supplierMapper.toEntity(request);
        supplier.setStatus(Status.ACTIVE);

        return supplierMapper.toResponse(supplierRepository.save(supplier));
    }

    @Override
    public List<SupplierResponse> findAll() {
        return supplierMapper.toResponseList(supplierRepository.findAll());
    }

    @Override
    public SupplierResponse findById(Long id) {
        Supplier supplier = findSupplierById(id);
        return supplierMapper.toResponse(supplier);
    }

    @Override
    public SupplierResponse update(Long id, SupplierRequest request) {
        Supplier supplier = findSupplierById(id);

        supplier.setName(request.name());
        supplier.setPhone(request.phone());
        supplier.setEmail(request.email());
        supplier.setAddress(request.address());

        return supplierMapper.toResponse(supplierRepository.save(supplier));
    }

    @Override
    public void delete(Long id) {
        Supplier supplier = findSupplierById(id);
        supplier.setStatus(Status.INACTIVE);
        supplierRepository.save(supplier);
    }

    private Supplier findSupplierById(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con id: " + id));
    }
}