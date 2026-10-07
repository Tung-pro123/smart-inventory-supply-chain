package com.tung.inventory.controller;

import com.tung.inventory.dto.response.ApiResponse;
import com.tung.inventory.service.CacheService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/cache")
@RequiredArgsConstructor
@Tag(name = "Cache Management", description = "Cache management and monitoring endpoints")
public class CacheController {

    private final CacheService cacheService;

    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get cache statistics", description = "Returns cache statistics including Redis key count")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getCacheStats() {
        CacheService.CacheStats stats = cacheService.getCacheStats();

        Map<String, Object> data = new HashMap<>();
        data.put("redisKeys", stats.getRedisKeys());
        data.put("cacheStats", stats.getCacheStats());

        return ResponseEntity.ok(ApiResponse.success(data));
    }

    @PostMapping("/evict/products")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Evict products cache", description = "Clears the products cache")
    public ResponseEntity<ApiResponse<String>> evictProductsCache() {
        cacheService.evictProductsCache();
        return ResponseEntity.ok(ApiResponse.success("Products cache evicted successfully"));
    }

    @PostMapping("/evict/warehouses")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Evict warehouses cache", description = "Clears the warehouses cache")
    public ResponseEntity<ApiResponse<String>> evictWarehousesCache() {
        cacheService.evictWarehousesCache();
        return ResponseEntity.ok(ApiResponse.success("Warehouses cache evicted successfully"));
    }

    @PostMapping("/evict/low-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE_MANAGER')")
    @Operation(summary = "Evict low stock cache", description = "Clears the low stock alerts cache")
    public ResponseEntity<ApiResponse<String>> evictLowStockCache() {
        cacheService.evictLowStockCache();
        return ResponseEntity.ok(ApiResponse.success("Low stock cache evicted successfully"));
    }

    @PostMapping("/evict/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE_MANAGER')")
    @Operation(summary = "Evict dashboard cache", description = "Clears the dashboard cache")
    public ResponseEntity<ApiResponse<String>> evictDashboardCache() {
        cacheService.evictDashboardCache();
        return ResponseEntity.ok(ApiResponse.success("Dashboard cache evicted successfully"));
    }

    @PostMapping("/evict/inventory/{productId}/{warehouseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE_MANAGER', 'OPERATOR')")
    @Operation(summary = "Evict inventory snapshot cache", description = "Clears cache for specific product and warehouse")
    public ResponseEntity<ApiResponse<String>> evictInventorySnapshotCache(
            @PathVariable Long productId,
            @PathVariable Long warehouseId) {
        cacheService.evictInventorySnapshotCache(productId, warehouseId);
        return ResponseEntity.ok(ApiResponse.success("Inventory snapshot cache evicted for product " + productId + " warehouse " + warehouseId));
    }

    @PostMapping("/warmup")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Warm up cache", description = "Preloads frequently accessed data into cache")
    public ResponseEntity<ApiResponse<String>> warmUpCache() {
        cacheService.warmUpCache();
        return ResponseEntity.ok(ApiResponse.success("Cache warm-up completed successfully"));
    }

    @PostMapping("/evict/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Evict all caches", description = "Clears all application caches")
    public ResponseEntity<ApiResponse<String>> evictAllCaches() {
        cacheService.evictAllCaches();
        return ResponseEntity.ok(ApiResponse.success("All caches evicted successfully"));
    }
}
