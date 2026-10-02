package com.tung.inventory.service;

import com.tung.inventory.dto.request.InboundRequest;
import com.tung.inventory.dto.request.OutboundRequest;
import com.tung.inventory.dto.request.AdjustmentRequest;
import com.tung.inventory.dto.response.InventoryEventResponse;
import com.tung.inventory.dto.response.InventorySnapshotResponse;
import com.tung.inventory.entity.InventoryEvent;
import com.tung.inventory.entity.InventorySnapshot;
import com.tung.inventory.entity.Product;
import com.tung.inventory.entity.Warehouse;
import com.tung.inventory.exception.InsufficientStockException;
import com.tung.inventory.exception.ResourceNotFoundException;
import com.tung.inventory.repository.InventoryEventRepository;
import com.tung.inventory.repository.InventorySnapshotRepository;
import com.tung.inventory.repository.ProductRepository;
import com.tung.inventory.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventorySnapshotRepository snapshotRepository;
    private final InventoryEventRepository eventRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;

    @Transactional
    public InventoryEventResponse processInbound(InboundRequest request, String username) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", request.getProductId()));

        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", "id", request.getWarehouseId()));

        InventorySnapshot snapshot = getOrCreateSnapshot(product, warehouse, request.getBinLocation());

        int quantityBefore = snapshot.getQuantityOnHand();
        snapshot.adjustQuantity(request.getQuantity());
        snapshot.setLastEventId(null);
        snapshot = snapshotRepository.save(snapshot);

        InventoryEvent event = InventoryEvent.builder()
                .snapshot(snapshot)
                .product(product)
                .warehouse(warehouse)
                .eventType(InventoryEvent.EventType.INBOUND)
                .quantityChange(request.getQuantity())
                .quantityBefore(quantityBefore)
                .quantityAfter(snapshot.getQuantityOnHand())
                .referenceNumber(request.getReferenceNumber())
                .reason(request.getReason())
                .performedBy(username)
                .eventTimestamp(LocalDateTime.now())
                .build();

        event = eventRepository.save(event);
        snapshot.setLastEventId(event.getId());
        snapshotRepository.save(snapshot);

        log.info("INBOUND: {} units of SKU {} to warehouse {} by {}",
                request.getQuantity(), product.getSku(), warehouse.getCode(), username);

        return mapToEventResponse(event);
    }

    @Transactional
    public InventoryEventResponse processOutbound(OutboundRequest request, String username) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", request.getProductId()));

        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", "id", request.getWarehouseId()));

        InventorySnapshot snapshot = snapshotRepository
                .findByProductIdAndWarehouseIdWithLock(product.getId(), warehouse.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Inventory snapshot not found"));

        int quantityAvailable = snapshot.getQuantityAvailable();
        if (quantityAvailable < request.getQuantity()) {
            throw new InsufficientStockException(request.getQuantity(), quantityAvailable);
        }

        int quantityBefore = snapshot.getQuantityOnHand();
        snapshot.adjustQuantity(-request.getQuantity());
        snapshot.setLastEventId(null);
        snapshot = snapshotRepository.save(snapshot);

        InventoryEvent event = InventoryEvent.builder()
                .snapshot(snapshot)
                .product(product)
                .warehouse(warehouse)
                .eventType(InventoryEvent.EventType.OUTBOUND)
                .quantityChange(-request.getQuantity())
                .quantityBefore(quantityBefore)
                .quantityAfter(snapshot.getQuantityOnHand())
                .referenceNumber(request.getReferenceNumber())
                .reason(request.getReason())
                .performedBy(username)
                .eventTimestamp(LocalDateTime.now())
                .build();

        event = eventRepository.save(event);
        snapshot.setLastEventId(event.getId());
        snapshotRepository.save(snapshot);

        log.info("OUTBOUND: {} units of SKU {} from warehouse {} by {}",
                request.getQuantity(), product.getSku(), warehouse.getCode(), username);

        return mapToEventResponse(event);
    }

    @Transactional
    public InventoryEventResponse adjustStock(AdjustmentRequest request, String username) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", request.getProductId()));

        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", "id", request.getWarehouseId()));

        InventorySnapshot snapshot = getOrCreateSnapshot(product, warehouse, null);

        int quantityBefore = snapshot.getQuantityOnHand();
        snapshot.adjustQuantity(request.getQuantityChange());
        snapshot.setLastEventId(null);
        snapshot = snapshotRepository.save(snapshot);

        InventoryEvent event = InventoryEvent.builder()
                .snapshot(snapshot)
                .product(product)
                .warehouse(warehouse)
                .eventType(InventoryEvent.EventType.ADJUSTMENT)
                .quantityChange(request.getQuantityChange())
                .quantityBefore(quantityBefore)
                .quantityAfter(snapshot.getQuantityOnHand())
                .referenceNumber(request.getReferenceNumber())
                .reason(request.getReason())
                .performedBy(username)
                .eventTimestamp(LocalDateTime.now())
                .build();

        event = eventRepository.save(event);
        snapshot.setLastEventId(event.getId());
        snapshotRepository.save(snapshot);

        log.info("ADJUSTMENT: {} units of SKU {} in warehouse {} by {}",
                request.getQuantityChange(), product.getSku(), warehouse.getCode(), username);

        return mapToEventResponse(event);
    }

    @Transactional(readOnly = true)
    public InventorySnapshotResponse getSnapshot(Long productId, Long warehouseId) {
        InventorySnapshot snapshot = snapshotRepository
                .findByProductIdAndWarehouseId(productId, warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory snapshot not found"));
        return mapToSnapshotResponse(snapshot);
    }

    @Transactional(readOnly = true)
    public List<InventorySnapshotResponse> getLowStockItems() {
        return snapshotRepository.findAllLowStock().stream()
                .map(this::mapToSnapshotResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InventoryEventResponse> getEventHistory(Long productId, int limit) {
        return eventRepository.findByProductId(productId).stream()
                .limit(limit)
                .map(this::mapToEventResponse)
                .collect(Collectors.toList());
    }

    private InventorySnapshot getOrCreateSnapshot(Product product, Warehouse warehouse, String binLocation) {
        return snapshotRepository.findByProductIdAndWarehouseId(product.getId(), warehouse.getId())
                .orElseGet(() -> {
                    InventorySnapshot newSnapshot = InventorySnapshot.builder()
                            .product(product)
                            .warehouse(warehouse)
                            .quantityOnHand(0)
                            .quantityReserved(0)
                            .binLocation(binLocation)
                            .build();
                    return snapshotRepository.save(newSnapshot);
                });
    }

    private InventoryEventResponse mapToEventResponse(InventoryEvent event) {
        return InventoryEventResponse.builder()
                .id(event.getId())
                .product(mapToProductResponse(event.getProduct()))
                .warehouse(mapToWarehouseResponse(event.getWarehouse()))
                .eventType(event.getEventType().name())
                .quantityChange(event.getQuantityChange())
                .quantityBefore(event.getQuantityBefore())
                .quantityAfter(event.getQuantityAfter())
                .referenceNumber(event.getReferenceNumber())
                .reason(event.getReason())
                .performedBy(event.getPerformedBy())
                .eventTimestamp(event.getEventTimestamp())
                .build();
    }

    private InventorySnapshotResponse mapToSnapshotResponse(InventorySnapshot snapshot) {
        return InventorySnapshotResponse.builder()
                .id(snapshot.getId())
                .product(mapToProductResponse(snapshot.getProduct()))
                .warehouse(mapToWarehouseResponse(snapshot.getWarehouse()))
                .quantityOnHand(snapshot.getQuantityOnHand())
                .quantityReserved(snapshot.getQuantityReserved())
                .quantityAvailable(snapshot.getQuantityAvailable())
                .binLocation(snapshot.getBinLocation())
                .stockStatus(snapshot.getStockStatus().name())
                .updatedAt(snapshot.getUpdatedAt())
                .build();
    }

    private com.tung.inventory.dto.response.ProductResponse mapToProductResponse(Product product) {
        return com.tung.inventory.dto.response.ProductResponse.builder()
                .id(product.getId())
                .sku(product.getSku())
                .name(product.getName())
                .status(product.getStatus().name())
                .build();
    }

    private com.tung.inventory.dto.response.WarehouseResponse mapToWarehouseResponse(Warehouse warehouse) {
        return com.tung.inventory.dto.response.WarehouseResponse.builder()
                .id(warehouse.getId())
                .code(warehouse.getCode())
                .name(warehouse.getName())
                .build();
    }
}
