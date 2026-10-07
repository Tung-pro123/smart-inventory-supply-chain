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
public class InventorySnapshotResponse {
    private Long id;
    private ProductResponse product;
    private WarehouseResponse warehouse;
    private Integer quantityOnHand;
    private Integer quantityReserved;
    private Integer quantityAvailable;
    private String binLocation;
    private String stockStatus;
    private LocalDateTime updatedAt;
}
