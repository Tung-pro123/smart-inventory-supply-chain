package com.tung.inventory.controller;

import com.tung.inventory.dto.request.InboundRequest;
import com.tung.inventory.dto.request.OutboundRequest;
import com.tung.inventory.dto.request.AdjustmentRequest;
import com.tung.inventory.dto.response.ApiResponse;
import com.tung.inventory.dto.response.InventoryEventResponse;
import com.tung.inventory.dto.response.InventorySnapshotResponse;
import com.tung.inventory.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("/inbound")
    public ResponseEntity<ApiResponse<InventoryEventResponse>> processInbound(
            @Valid @RequestBody InboundRequest request,
            @AuthenticationPrincipal UserDetails user) {
        InventoryEventResponse response = inventoryService.processInbound(request, user.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Inbound processed successfully", response));
    }

    @PostMapping("/outbound")
    public ResponseEntity<ApiResponse<InventoryEventResponse>> processOutbound(
            @Valid @RequestBody OutboundRequest request,
            @AuthenticationPrincipal UserDetails user) {
        InventoryEventResponse response = inventoryService.processOutbound(request, user.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Outbound processed successfully", response));
    }

    @PostMapping("/adjust")
    public ResponseEntity<ApiResponse<InventoryEventResponse>> adjustStock(
            @Valid @RequestBody AdjustmentRequest request,
            @AuthenticationPrincipal UserDetails user) {
        InventoryEventResponse response = inventoryService.adjustStock(request, user.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Adjustment processed successfully", response));
    }

    @GetMapping("/snapshot")
    public ResponseEntity<ApiResponse<InventorySnapshotResponse>> getSnapshot(
            @RequestParam Long productId,
            @RequestParam Long warehouseId) {
        InventorySnapshotResponse response = inventoryService.getSnapshot(productId, warehouseId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/low-stock")
    public ResponseEntity<ApiResponse<List<InventorySnapshotResponse>>> getLowStockItems() {
        List<InventorySnapshotResponse> response = inventoryService.getLowStockItems();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/events/{productId}")
    public ResponseEntity<ApiResponse<List<InventoryEventResponse>>> getEventHistory(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "50") int limit) {
        List<InventoryEventResponse> response = inventoryService.getEventHistory(productId, limit);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
