package edu.usta.groccy.service;

import edu.usta.groccy.dto.supply.SupplyRequest;
import edu.usta.groccy.dto.supply.SupplyResponse;

import java.util.List;

public interface SupplyService {

    SupplyResponse create(SupplyRequest request);

    List<SupplyResponse> findAll();

    SupplyResponse findById(Long id);

    SupplyResponse update(Long id, SupplyRequest request);

    void delete(Long id);
}