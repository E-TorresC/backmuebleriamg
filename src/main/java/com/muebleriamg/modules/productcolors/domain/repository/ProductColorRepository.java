package com.muebleriamg.modules.productcolors.domain.repository;

import com.muebleriamg.modules.productcolors.domain.entity.ProductColor;
import com.muebleriamg.shared.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductColorRepository extends JpaRepository<ProductColor, Long> {

  List<ProductColor> findAllByStatus(Status status);

  Optional<ProductColor> findByProductColorIdAndStatus(Long productColorId, Status status);

  // Mejora: evitar duplicidad por (ProductID, ColorID)
  Optional<ProductColor> findByProduct_ProductIdAndColor_ColorId(Long productId, Long colorId);

  boolean existsByProduct_ProductIdAndColor_ColorIdAndProductColorIdNot(Long productId, Long colorId, Long productColorId);

  // Filtros
  List<ProductColor> findAllByProduct_ProductIdAndStatus(Long productId, Status status);

  List<ProductColor> findAllByColor_ColorIdAndStatus(Long colorId, Status status);

  List<ProductColor> findAllByProduct_ProductIdAndColor_ColorIdAndStatus(Long productId, Long colorId, Status status);

  //USO POR STOCKBYVARIANT
  boolean existsByProduct_ProductIdAndColor_ColorIdAndStatus(Long productId, Long colorId, com.muebleriamg.shared.enums.Status status);

}
