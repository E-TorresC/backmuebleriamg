package com.muebleriamg.modules.productmeasures.domain.repository;

import com.muebleriamg.modules.productmeasures.domain.entity.ProductMeasure;
import com.muebleriamg.shared.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductMeasureRepository extends JpaRepository<ProductMeasure, Long> {

  List<ProductMeasure> findAllByStatus(Status status);

  Optional<ProductMeasure> findByProductMeasureIdAndStatus(Long productMeasureId, Status status);

  Optional<ProductMeasure> findByProduct_ProductIdAndMeasure_MeasureId(Long productId, Long measureId);

  boolean existsByProduct_ProductIdAndMeasure_MeasureIdAndProductMeasureIdNot(Long productId, Long measureId, Long productMeasureId);

  List<ProductMeasure> findAllByProduct_ProductIdAndStatus(Long productId, Status status);

  List<ProductMeasure> findAllByMeasure_MeasureIdAndStatus(Long measureId, Status status);

  List<ProductMeasure> findAllByProduct_ProductIdAndMeasure_MeasureIdAndStatus(Long productId, Long measureId, Status status);

  //PARTE DE STOCKBYVARIANT
  boolean existsByProduct_ProductIdAndMeasure_MeasureIdAndStatus(Long productId, Long measureId, com.muebleriamg.shared.enums.Status status);

}
