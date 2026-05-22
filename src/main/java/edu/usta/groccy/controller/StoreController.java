package edu.usta.groccy.controller;

import edu.usta.groccy.dto.store.StoreRequest;
import edu.usta.groccy.dto.store.StoreResponse;
import edu.usta.groccy.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/locales")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StoreResponse create(@Valid @RequestBody StoreRequest request) {
        return storeService.create(request);
    }

    @GetMapping
    public List<StoreResponse> findAll() {
        return storeService.findAll();
    }

    @GetMapping("/{id}")
    public StoreResponse findById(@PathVariable Long id) {
        return storeService.findById(id);
    }

    @PutMapping("/{id}")
    public StoreResponse update(@PathVariable Long id, @Valid @RequestBody StoreRequest request) {
        return storeService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        storeService.delete(id);
    }
}