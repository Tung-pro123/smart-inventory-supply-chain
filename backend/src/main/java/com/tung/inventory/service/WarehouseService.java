package com.tung.inventory.service;

import com.tung.inventory.dto.response.ProductResponse;
import com.tung.inventory.dto.response.WarehouseResponse;
import com.tung.inventory.entity.Product;
import com.tung.inventory.entity.Warehouse;
import com.tung.inventory.exception.BusinessException;
import com.tung.inventory.exception.ResourceNotFoundException;
import com.tung.inventory.repository.ProductRepository;
import com.tung.inventory.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    @Cacheable(value = "warehouses", key = "'all'")
    public List<WarehouseResponse> getAllWarehouses() {
        log.debug("Fetching all warehouses (cache miss)");
        return warehouseRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "warehouses", key = "#id")
    public WarehouseResponse getWarehouseById(Long id) {
        log.debug("Fetching warehouse by id {} (cache miss)", id);
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", "id", id));
        return mapToResponse(warehouse);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "warehouses", key = "'code:' + #code")
    public WarehouseResponse getWarehouseByCode(String code) {
        log.debug("Fetching warehouse by code {} (cache miss)", code);
        Warehouse warehouse = warehouseRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", "code", code));
        return mapToResponse(warehouse);
    }

    @Transactional
    @CacheEvict(value = "warehouses", allEntries = true)
    public WarehouseResponse createWarehouse(com.tung.inventory.dto.request.WarehouseRequest request) {
        if (warehouseRepository.existsByCode(request.getCode())) {
            throw new BusinessException("CODE_EXISTS", "Warehouse code already exists");
        }

        Warehouse warehouse = Warehouse.builder()
                .code(request.getCode())
                .name(request.getName())
                .location(request.getLocation())
                .description(request.getDescription())
                .status(parseStatus(request.getStatus()))
                .build();

        warehouse = warehouseRepository.save(warehouse);
        log.info("Created warehouse: {}", warehouse.getCode());
        return mapToResponse(warehouse);
    }

    @Transactional
    @CacheEvict(value = "warehouses", allEntries = true)
    public WarehouseResponse updateWarehouse(Long id, com.tung.inventory.dto.request.WarehouseRequest request) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", "id", id));

        if (!warehouse.getCode().equals(request.getCode()) &&
            warehouseRepository.existsByCode(request.getCode())) {
            throw new BusinessException("CODE_EXISTS", "Warehouse code already exists");
        }

        warehouse.setCode(request.getCode());
        warehouse.setName(request.getName());
        warehouse.setLocation(request.getLocation());
        warehouse.setDescription(request.getDescription());
        if (request.getStatus() != null) {
            warehouse.setStatus(parseStatus(request.getStatus()));
        }

        warehouse = warehouseRepository.save(warehouse);
        log.info("Updated warehouse: {}", warehouse.getCode());
        return mapToResponse(warehouse);
    }

    @Transactional
    @CacheEvict(value = "warehouses", allEntries = true)
    public void deleteWarehouse(Long id) {
        if (!warehouseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Warehouse", "id", id);
        }
        warehouseRepository.deleteById(id);
        log.info("Deleted warehouse id: {}", id);
    }

    private WarehouseResponse mapToResponse(Warehouse warehouse) {
        return WarehouseResponse.builder()
                .id(warehouse.getId())
                .code(warehouse.getCode())
                .name(warehouse.getName())
                .location(warehouse.getLocation())
                .status(warehouse.getStatus().name())
                .description(warehouse.getDescription())
                .createdAt(warehouse.getCreatedAt())
                .updatedAt(warehouse.getUpdatedAt())
                .build();
    }

    private Warehouse.WarehouseStatus parseStatus(String status) {
        if (status == null) return Warehouse.WarehouseStatus.ACTIVE;
        try {
            return Warehouse.WarehouseStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Warehouse.WarehouseStatus.ACTIVE;
        }
    }
}
