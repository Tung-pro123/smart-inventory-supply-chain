package com.tung.inventory.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryEventResponse {
    private Long id;
    private ProductResponse product;
    private WarehouseResponse warehouse;
    private String eventType;
    private Integer quantityChange;
    private Integer quantityBefore;
    private Integer quantityAfter;
    private String referenceNumber;
    private String reason;
    private String performedBy;
    private LocalDateTime eventTimestamp;
}
