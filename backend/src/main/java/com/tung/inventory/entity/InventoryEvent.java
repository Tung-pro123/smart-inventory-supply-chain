package com.tung.inventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_events",
       indexes = {
           @Index(name = "idx_event_snapshot", columnList = "snapshot_id"),
           @Index(name = "idx_event_product", columnList = "product_id"),
           @Index(name = "idx_event_type", columnList = "event_type"),
           @Index(name = "idx_event_timestamp", columnList = "event_timestamp"),
           @Index(name = "idx_event_reference", columnList = "reference_number")
       })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Snapshot reference is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "snapshot_id", nullable = false)
    private InventorySnapshot snapshot;

    @NotNull(message = "Product is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotNull(message = "Warehouse is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @NotNull(message = "Event type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false,
            columnDefinition = "ENUM('INBOUND', 'OUTBOUND', 'ADJUSTMENT', 'TRANSFER', 'RECOUNT', 'DAMAGE', 'RETURN')")
    private EventType eventType;

    @NotNull(message = "Quantity change is required")
    @Column(name = "quantity_change", nullable = false)
    private Integer quantityChange;

    @Column(name = "quantity_before")
    private Integer quantityBefore;

    @Column(name = "quantity_after")
    private Integer quantityAfter;

    @NotBlank(message = "Reference number is required")
    @Size(max = 100)
    @Column(name = "reference_number", nullable = false)
    private String referenceNumber;

    @Size(max = 500)
    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(name = "performed_by")
    private String performedBy;

    @Column(name = "event_timestamp", nullable = false)
    private LocalDateTime eventTimestamp;

    @Column(columnDefinition = "TEXT")
    private String metadata;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (eventTimestamp == null) {
            eventTimestamp = LocalDateTime.now();
        }
    }

    public enum EventType {
        INBOUND,      // Goods received into warehouse
        OUTBOUND,     // Goods shipped out
        ADJUSTMENT,   // Manual stock correction
        TRANSFER,     // Inter-warehouse transfer
        RECOUNT,      // Physical inventory count
        DAMAGE,       // Damaged goods write-off
        RETURN        // Customer returns
    }
}
