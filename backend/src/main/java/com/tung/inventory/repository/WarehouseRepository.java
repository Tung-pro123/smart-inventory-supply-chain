package com.tung.inventory.repository;

import com.tung.inventory.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    Optional<Warehouse> findByCode(String code);

    boolean existsByCode(String code);

    @Query("SELECT w FROM Warehouse w WHERE w.status = 'ACTIVE'")
    List<Warehouse> findAllActive();

    @Query("SELECT w FROM Warehouse w WHERE w.status = 'ACTIVE' ORDER BY w.name")
    List<Warehouse> findAllActiveOrderByName();
}
