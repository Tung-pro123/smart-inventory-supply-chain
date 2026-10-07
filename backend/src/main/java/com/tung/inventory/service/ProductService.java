package com.tung.inventory.service;

import com.tung.inventory.dto.request.ProductRequest;
import com.tung.inventory.dto.response.PageResponse;
import com.tung.inventory.dto.response.ProductResponse;
import com.tung.inventory.entity.Category;
import com.tung.inventory.entity.Product;
import com.tung.inventory.exception.BusinessException;
import com.tung.inventory.exception.ResourceNotFoundException;
import com.tung.inventory.repository.CategoryRepository;
import com.tung.inventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    @Cacheable(value = "products", key = "'all'")
    public List<ProductResponse> getAllProducts() {
        log.debug("Fetching all products (cache miss)");
        return productRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PageResponse<ProductResponse> getProducts(Pageable pageable) {
        Page<Product> page = productRepository.findAll(pageable);
        return mapToPageResponse(page);
    }

    @Transactional(readOnly = true)
    public PageResponse<ProductResponse> searchProducts(String keyword, Pageable pageable) {
        Page<Product> page = productRepository.searchByKeyword(keyword, pageable);
        return mapToPageResponse(page);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "products", key = "#id")
    public ProductResponse getProductById(Long id) {
        log.debug("Fetching product by id {} (cache miss)", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        return mapToResponse(product);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "products", key = "'sku:' + #sku")
    public ProductResponse getProductBySku(String sku) {
        log.debug("Fetching product by SKU {} (cache miss)", sku);
        Product product = productRepository.findBySku(sku)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "sku", sku));
        return mapToResponse(product);
    }

    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public ProductResponse createProduct(ProductRequest request) {
        if (productRepository.existsBySku(request.getSku())) {
            throw new BusinessException("SKU_EXISTS", "Product SKU already exists");
        }

        Product product = Product.builder()
                .sku(request.getSku())
                .name(request.getName())
                .description(request.getDescription())
                .unitOfMeasure(parseUnit(request.getUnitOfMeasure()))
                .weight(request.getWeight())
                .manufacturer(request.getManufacturer())
                .minStockLevel(request.getMinStockLevel() != null ? request.getMinStockLevel() : 0)
                .maxStockLevel(request.getMaxStockLevel() != null ? request.getMaxStockLevel() : 10000)
                .status(parseProductStatus(request.getStatus()))
                .build();

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));
            product.setCategory(category);
        }

        product = productRepository.save(product);
        log.info("Created product: {}", product.getSku());
        return mapToResponse(product);
    }

    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        if (!product.getSku().equals(request.getSku()) &&
            productRepository.existsBySku(request.getSku())) {
            throw new BusinessException("SKU_EXISTS", "Product SKU already exists");
        }

        product.setSku(request.getSku());
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setUnitOfMeasure(parseUnit(request.getUnitOfMeasure()));
        product.setWeight(request.getWeight());
        product.setManufacturer(request.getManufacturer());
        if (request.getMinStockLevel() != null) {
            product.setMinStockLevel(request.getMinStockLevel());
        }
        if (request.getMaxStockLevel() != null) {
            product.setMaxStockLevel(request.getMaxStockLevel());
        }
        if (request.getStatus() != null) {
            product.setStatus(parseProductStatus(request.getStatus()));
        }

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));
            product.setCategory(category);
        }

        product = productRepository.save(product);
        log.info("Updated product: {}", product.getSku());
        return mapToResponse(product);
    }

    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product", "id", id);
        }
        productRepository.deleteById(id);
        log.info("Deleted product id: {}", id);
    }

    private ProductResponse mapToResponse(Product product) {
        ProductResponse.ProductResponseBuilder builder = ProductResponse.builder()
                .id(product.getId())
                .sku(product.getSku())
                .name(product.getName())
                .description(product.getDescription())
                .unitOfMeasure(product.getUnitOfMeasure().name())
                .weight(product.getWeight())
                .manufacturer(product.getManufacturer())
                .minStockLevel(product.getMinStockLevel())
                .maxStockLevel(product.getMaxStockLevel())
                .status(product.getStatus().name())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt());

        if (product.getCategory() != null) {
            builder.category(ProductResponse.CategoryResponse.builder()
                    .id(product.getCategory().getId())
                    .name(product.getCategory().getName())
                    .build());
        }

        return builder.build();
    }

    private PageResponse<ProductResponse> mapToPageResponse(Page<Product> page) {
        return PageResponse.<ProductResponse>builder()
                .content(page.getContent().stream().map(this::mapToResponse).collect(Collectors.toList()))
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

    private Product.UnitOfMeasure parseUnit(String unit) {
        if (unit == null) return Product.UnitOfMeasure.PIECE;
        try {
            return Product.UnitOfMeasure.valueOf(unit.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Product.UnitOfMeasure.PIECE;
        }
    }

    private Product.ProductStatus parseProductStatus(String status) {
        if (status == null) return Product.ProductStatus.ACTIVE;
        try {
            return Product.ProductStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Product.ProductStatus.ACTIVE;
        }
    }
}
