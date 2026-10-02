package com.tung.inventory.repository;

import com.tung.inventory.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findBySku(String sku);

    boolean existsBySku(String sku);

    @Query("SELECT p FROM Product p WHERE p.status = 'ACTIVE'")
    List<Product> findAllActive();

    @Query("SELECT p FROM Product p WHERE p.status = 'ACTIVE'")
    Page<Product> findAllActive(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(p.sku) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Product> searchByKeyword(String keyword, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId AND p.status = 'ACTIVE'")
    List<Product> findByCategoryId(Long categoryId);
}
