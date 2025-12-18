package com.muebleriamg.modules.warehouses.domain.repository;

import com.muebleriamg.modules.warehouses.domain.entity.Warehouse;
import com.muebleriamg.shared.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

  List<Warehouse> findAllByStatus(Status status);

  Optional<Warehouse> findByWarehouseIdAndStatus(Long warehouseId, Status status);

  boolean existsByWarehouseNameIgnoreCase(String warehouseName);

  boolean existsByWarehouseSlugIgnoreCase(String warehouseSlug);

  boolean existsByWarehouseNameIgnoreCaseAndWarehouseIdNot(String warehouseName, Long warehouseId);

  boolean existsByWarehouseSlugIgnoreCaseAndWarehouseIdNot(String warehouseSlug, Long warehouseId);
}
