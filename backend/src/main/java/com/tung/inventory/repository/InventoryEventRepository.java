package com.tung.inventory.repository;

import com.tung.inventory.entity.InventoryEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryEventRepository extends JpaRepository<InventoryEvent, Long> {

    @Query("SELECT e FROM InventoryEvent e WHERE e.snapshot.id = :snapshotId ORDER BY e.eventTimestamp DESC")
    List<InventoryEvent> findBySnapshotId(Long snapshotId);

    @Query("SELECT e FROM InventoryEvent e WHERE e.product.id = :productId ORDER BY e.eventTimestamp DESC")
    List<InventoryEvent> findByProductId(Long productId);

    @Query("SELECT e FROM InventoryEvent e WHERE e.product.id = :productId ORDER BY e.eventTimestamp DESC")
    Page<InventoryEvent> findByProductId(Long productId, Pageable pageable);

    @Query("SELECT e FROM InventoryEvent e WHERE e.warehouse.id = :warehouseId ORDER BY e.eventTimestamp DESC")
    Page<InventoryEvent> findByWarehouseId(Long warehouseId, Pageable pageable);

    @Query("SELECT e FROM InventoryEvent e WHERE e.referenceNumber = :referenceNumber")
    Optional<InventoryEvent> findByReferenceNumber(String referenceNumber);

    @Query("SELECT e FROM InventoryEvent e WHERE e.eventTimestamp BETWEEN :startDate AND :endDate ORDER BY e.eventTimestamp DESC")
    List<InventoryEvent> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT e FROM InventoryEvent e WHERE e.eventTimestamp BETWEEN :startDate AND :endDate ORDER BY e.eventTimestamp DESC")
    Page<InventoryEvent> findByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    @Query("SELECT e FROM InventoryEvent e WHERE e.eventType = :eventType AND e.eventTimestamp BETWEEN :startDate AND :endDate")
    List<InventoryEvent> findByEventTypeAndDateRange(
            InventoryEvent.EventType eventType,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    @Query("SELECT COUNT(e) FROM InventoryEvent e WHERE e.eventTimestamp >= :since")
    long countEventsSince(LocalDateTime since);
}
