package com.muebleriamg.modules.stockbyvariant.domain.repository;

import com.muebleriamg.modules.stockbyvariant.domain.entity.StockByVariant;
import com.muebleriamg.shared.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import jakarta.persistence.LockModeType;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StockByVariantRepository extends JpaRepository<StockByVariant, Long> {

  List<StockByVariant> findAllByStatus(Status status);

  Optional<StockByVariant> findByStockIdAndStatus(Long stockId, Status status);

  Optional<StockByVariant> findByProduct_ProductIdAndMeasure_MeasureIdAndColor_ColorIdAndWarehouse_WarehouseId(
    Long productId, Long measureId, Long colorId, Long warehouseId
  );

  boolean existsByProduct_ProductIdAndMeasure_MeasureIdAndColor_ColorIdAndWarehouse_WarehouseIdAndStockIdNot(
    Long productId, Long measureId, Long colorId, Long warehouseId, Long stockId
  );

  List<StockByVariant> findAllByWarehouse_WarehouseIdAndStatus(Long warehouseId, Status status);

  List<StockByVariant> findAllByProduct_ProductIdAndStatus(Long productId, Status status);

  List<StockByVariant> findAllByProduct_ProductIdAndWarehouse_WarehouseIdAndStatus(Long productId, Long warehouseId, Status status);

  List<StockByVariant> findAllByProduct_ProductIdAndMeasure_MeasureIdAndColor_ColorIdAndWarehouse_WarehouseIdAndStatus(
    Long productId, Long measureId, Long colorId, Long warehouseId, Status status
  );

  /** Para ajustes de stock con bloqueo (evita carreras en concurrencia). */
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("""
    select s from StockByVariant s
    where s.stockId = :stockId
  """)
  Optional<StockByVariant> findByIdForUpdate(Long stockId);

  @Query("""
    select coalesce(sum(s.quantity), 0)
    from StockByVariant s
    where s.status = com.muebleriamg.shared.enums.Status.ACTIVE
      and s.product.productId = :productId
      and s.measure.measureId = :measureId
      and s.color.colorId = :colorId
      and s.warehouse.status = com.muebleriamg.shared.enums.Status.ACTIVE
  """)
  long sumAvailableStockAllActiveWarehouses(@Param("productId") Long productId,
                                            @Param("measureId") Long measureId,
                                            @Param("colorId") Long colorId);

  @Query("""
    select coalesce(sum(s.quantity), 0)
    from StockByVariant s
    where s.status = com.muebleriamg.shared.enums.Status.ACTIVE
      and s.product.productId = :productId
      and s.measure.measureId = :measureId
      and s.color.colorId = :colorId
      and s.warehouse.warehouseId = :warehouseId
      and s.warehouse.status = com.muebleriamg.shared.enums.Status.ACTIVE
  """)
  long sumAvailableStockByWarehouse(@Param("productId") Long productId,
                                    @Param("measureId") Long measureId,
                                    @Param("colorId") Long colorId,
                                    @Param("warehouseId") Long warehouseId);
}
