package com.muebleriamg.modules.measures.domain.repository;

import com.muebleriamg.modules.measures.domain.entity.Measure;
import com.muebleriamg.shared.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface MeasureRepository extends JpaRepository<Measure, Long> {

  List<Measure> findAllByStatus(Status status);

  Optional<Measure> findByMeasureIdAndStatus(Long measureId, Status status);

  boolean existsByWidthCmAndHeightCmAndDepthCm(BigDecimal widthCm, BigDecimal heightCm, BigDecimal depthCm);

  boolean existsByWidthCmAndHeightCmAndDepthCmAndMeasureIdNot(
    BigDecimal widthCm, BigDecimal heightCm, BigDecimal depthCm, Long measureId
  );
}

