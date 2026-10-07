package com.tung.inventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_snapshots",
       uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "warehouse_id"}),
       indexes = {
           @Index(name = "idx_snapshot_product", columnList = "product_id"),
           @Index(name = "idx_snapshot_warehouse", columnList = "warehouse_id"),
           @Index(name = "idx_snapshot_status", columnList = "stock_status")
       })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventorySnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Product is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotNull(message = "Warehouse is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Column(name = "quantity_on_hand", nullable = false)
    @Builder.Default
    private Integer quantityOnHand = 0;

    @Column(name = "quantity_reserved")
    @Builder.Default
    private Integer quantityReserved = 0;

    @Column(name = "quantity_available", insertable = false, updatable = false)
    private Integer quantityAvailable;

    @Column(name = "bin_location", length = 50)
    private String binLocation;

    @Enumerated(EnumType.STRING)
    @Column(name = "stock_status", columnDefinition = "ENUM('OPTIMAL', 'LOW_STOCK', 'CRITICAL', 'OUT_OF_STOCK')")
    @Builder.Default
    private StockStatus stockStatus = StockStatus.OPTIMAL;

    @Version
    private Long version;

    @Column(name = "last_event_id")
    private Long lastEventId;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        updateStockStatus();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        updateStockStatus();
    }

    public Integer getQuantityAvailable() {
        int available = quantityOnHand - quantityReserved;
        return Math.max(0, available);
    }

    public void updateStockStatus() {
        int available = getQuantityAvailable();
        if (available <= 0) {
            stockStatus = StockStatus.OUT_OF_STOCK;
        } else if (product != null && product.getMinStockLevel() != null
                   && available <= product.getMinStockLevel()) {
            stockStatus = StockStatus.CRITICAL;
        } else if (product != null && product.getMinStockLevel() != null
                   && available <= product.getMinStockLevel() * 2) {
            stockStatus = StockStatus.LOW_STOCK;
        } else {
            stockStatus = StockStatus.OPTIMAL;
        }
    }

    public void adjustQuantity(int delta) {
        this.quantityOnHand = Math.max(0, this.quantityOnHand + delta);
        updateStockStatus();
    }

    public enum StockStatus {
        OPTIMAL, LOW_STOCK, CRITICAL, OUT_OF_STOCK
    }
}
