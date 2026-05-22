package edu.usta.groccy.service.impl;

import edu.usta.groccy.dto.store.StoreRequest;
import edu.usta.groccy.dto.store.StoreResponse;
import edu.usta.groccy.entity.Store;
import edu.usta.groccy.enums.Status;
import edu.usta.groccy.exception.ResourceNotFoundException;
import edu.usta.groccy.mapper.StoreMapper;
import edu.usta.groccy.repository.StoreRepository;
import edu.usta.groccy.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;

    @Override
    public StoreResponse create(StoreRequest request) {
        Store store = storeMapper.toEntity(request);
        store.setStatus(Status.ACTIVE);

        return storeMapper.toResponse(storeRepository.save(store));
    }

    @Override
    public List<StoreResponse> findAll() {
        return storeMapper.toResponseList(storeRepository.findAll());
    }

    @Override
    public StoreResponse findById(Long id) {
        Store store = findStoreById(id);
        return storeMapper.toResponse(store);
    }

    @Override
    public StoreResponse update(Long id, StoreRequest request) {
        Store store = findStoreById(id);

        store.setName(request.name());
        store.setAddress(request.address());
        store.setZone(request.zone());
        store.setType(request.type());

        return storeMapper.toResponse(storeRepository.save(store));
    }

    @Override
    public void delete(Long id) {
        Store store = findStoreById(id);
        store.setStatus(Status.INACTIVE);
        storeRepository.save(store);
    }

    private Store findStoreById(Long id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Local no encontrado con id: " + id));
    }
}