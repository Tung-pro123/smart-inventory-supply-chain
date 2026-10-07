package com.tung.inventory.controller;

import com.tung.inventory.dto.response.ApiResponse;
import com.tung.inventory.entity.InventoryEvent;
import com.tung.inventory.entity.InventorySnapshot;
import com.tung.inventory.repository.InventoryEventRepository;
import com.tung.inventory.repository.InventorySnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final InventorySnapshotRepository snapshotRepository;
    private final InventoryEventRepository eventRepository;

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardMetrics() {
        Map<String, Object> metrics = new HashMap<>();

        // Count total products
        long totalProducts = snapshotRepository.count();
        metrics.put("totalProducts", totalProducts);

        // Count by status
        long optimalCount = snapshotRepository.countByStockStatus(InventorySnapshot.StockStatus.OPTIMAL);
        long lowStockCount = snapshotRepository.countByStockStatus(InventorySnapshot.StockStatus.LOW_STOCK);
        long criticalCount = snapshotRepository.countByStockStatus(InventorySnapshot.StockStatus.CRITICAL);
        long outOfStockCount = snapshotRepository.countByStockStatus(InventorySnapshot.StockStatus.OUT_OF_STOCK);

        Map<String, Long> stockStatusBreakdown = new HashMap<>();
        stockStatusBreakdown.put("optimal", optimalCount);
        stockStatusBreakdown.put("lowStock", lowStockCount);
        stockStatusBreakdown.put("critical", criticalCount);
        stockStatusBreakdown.put("outOfStock", outOfStockCount);
        metrics.put("stockStatusBreakdown", stockStatusBreakdown);

        // Events in last 24h
        LocalDateTime since24h = LocalDateTime.now().minusHours(24);
        long events24h = eventRepository.countEventsSince(since24h);
        metrics.put("eventsLast24h", events24h);

        // Low stock alerts
        long lowStockAlerts = lowStockCount + criticalCount + outOfStockCount;
        metrics.put("lowStockAlerts", lowStockAlerts);

        return ResponseEntity.ok(ApiResponse.success(metrics));
    }

    @GetMapping("/events/velocity")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getEventVelocity(
            @RequestParam(defaultValue = "7") int days) {

        LocalDateTime startDate = LocalDateTime.now().minusDays(days);

        long inboundCount = eventRepository.findByEventTypeAndDateRange(
                InventoryEvent.EventType.INBOUND, startDate, LocalDateTime.now()).size();
        long outboundCount = eventRepository.findByEventTypeAndDateRange(
                InventoryEvent.EventType.OUTBOUND, startDate, LocalDateTime.now()).size();

        Map<String, Object> velocity = new HashMap<>();
        velocity.put("inboundTotal", inboundCount);
        velocity.put("outboundTotal", outboundCount);
        velocity.put("netChange", inboundCount - outboundCount);
        velocity.put("periodDays", days);

        return ResponseEntity.ok(ApiResponse.success(velocity));
    }
}
