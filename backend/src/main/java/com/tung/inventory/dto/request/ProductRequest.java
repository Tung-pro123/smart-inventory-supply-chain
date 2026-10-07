package com.tung.inventory.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    @NotBlank(message = "SKU is required")
    @Size(max = 50, message = "SKU must not exceed 50 characters")
    private String sku;

    @NotBlank(message = "Product name is required")
    @Size(max = 200, message = "Name must not exceed 200 characters")
    private String name;

    private String description;

    private Long categoryId;

    private String unitOfMeasure;

    private BigDecimal weight;

    @Size(max = 100, message = "Manufacturer must not exceed 100 characters")
    private String manufacturer;

    private Integer minStockLevel;

    private Integer maxStockLevel;

    private String status;
}
