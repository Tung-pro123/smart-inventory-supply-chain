package com.tung.inventory.repository;

import com.tung.inventory.entity.InventorySnapshot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventorySnapshotRepository extends JpaRepository<InventorySnapshot, Long> {

    @Query("SELECT s FROM InventorySnapshot s WHERE s.product.id = :productId AND s.warehouse.id = :warehouseId")
    Optional<InventorySnapshot> findByProductIdAndWarehouseId(Long productId, Long warehouseId);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT s FROM InventorySnapshot s WHERE s.product.id = :productId AND s.warehouse.id = :warehouseId")
    Optional<InventorySnapshot> findByProductIdAndWarehouseIdWithLock(Long productId, Long warehouseId);

    @Query("SELECT s FROM InventorySnapshot s WHERE s.warehouse.id = :warehouseId")
    List<InventorySnapshot> findByWarehouseId(Long warehouseId);

    @Query("SELECT s FROM InventorySnapshot s WHERE s.warehouse.id = :warehouseId")
    Page<InventorySnapshot> findByWarehouseId(Long warehouseId, Pageable pageable);

    @Query("SELECT s FROM InventorySnapshot s WHERE s.product.id = :productId")
    List<InventorySnapshot> findByProductId(Long productId);

    @Query("SELECT s FROM InventorySnapshot s WHERE s.stockStatus IN ('LOW_STOCK', 'CRITICAL', 'OUT_OF_STOCK')")
    List<InventorySnapshot> findAllLowStock();

    @Query("SELECT s FROM InventorySnapshot s WHERE s.stockStatus IN ('LOW_STOCK', 'CRITICAL', 'OUT_OF_STOCK')")
    Page<InventorySnapshot> findAllLowStock(Pageable pageable);

    @Query("SELECT s FROM InventorySnapshot s WHERE s.warehouse.id = :warehouseId " +
           "AND s.stockStatus IN ('LOW_STOCK', 'CRITICAL', 'OUT_OF_STOCK')")
    List<InventorySnapshot> findLowStockByWarehouseId(Long warehouseId);

    @Query("SELECT COUNT(s) FROM InventorySnapshot s WHERE s.stockStatus = :status")
    long countByStockStatus(InventorySnapshot.StockStatus status);
}
