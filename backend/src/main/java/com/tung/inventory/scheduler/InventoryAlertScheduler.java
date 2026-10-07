package com.tung.inventory.scheduler;

import com.tung.inventory.entity.InventorySnapshot;
import com.tung.inventory.repository.InventorySnapshotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class InventoryAlertScheduler {

    private final InventorySnapshotRepository snapshotRepository;

    @Scheduled(cron = "0 0 8 * * *")
    public void checkLowStockAlerts() {
        log.info("Running daily low stock alert check");

        List<InventorySnapshot> lowStockItems = snapshotRepository.findAllLowStock();

        for (InventorySnapshot snapshot : lowStockItems) {
            String productName = snapshot.getProduct().getName();
            String warehouseCode = snapshot.getWarehouse().getCode();
            int available = snapshot.getQuantityAvailable();
            String status = snapshot.getStockStatus().name();

            log.warn("LOW STOCK ALERT - Product: {}, Warehouse: {}, Available: {}, Status: {}",
                    productName, warehouseCode, available, status);
        }

        log.info("Low stock alert check completed. Found {} items needing attention", lowStockItems.size());
    }

    @Scheduled(cron = "0 0 */6 * * *")
    public void syncStockStatuses() {
        log.info("Running stock status sync");

        List<InventorySnapshot> allSnapshots = snapshotRepository.findAll();
        int updated = 0;

        for (InventorySnapshot snapshot : allSnapshots) {
            int availableBefore = snapshot.getQuantityAvailable();
            snapshot.updateStockStatus();
            int availableAfter = snapshot.getQuantityAvailable();

            if (availableBefore != availableAfter) {
                snapshotRepository.save(snapshot);
                updated++;
            }
        }

        if (updated > 0) {
            log.info("Stock status sync completed. Updated {} snapshots", updated);
        }
    }
}
