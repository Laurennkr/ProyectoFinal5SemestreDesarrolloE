package edu.usta.groccy.service;

import edu.usta.groccy.dto.store.StoreRequest;
import edu.usta.groccy.dto.store.StoreResponse;

import java.util.List;

public interface StoreService {

    StoreResponse create(StoreRequest request);
    List<StoreResponse> findAll();
    StoreResponse findById(Long id);
    StoreResponse update(Long id, StoreRequest request);
    void delete(Long id);
}