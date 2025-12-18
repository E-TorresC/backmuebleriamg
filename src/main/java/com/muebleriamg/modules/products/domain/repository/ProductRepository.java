package com.muebleriamg.modules.products.domain.repository;

import com.muebleriamg.modules.products.domain.entity.Product;
import com.muebleriamg.shared.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

  List<Product> findAllByStatus(Status status);

  Optional<Product> findByProductIdAndStatus(Long productId, Status status);

  // UQ_Products (CategoryID, ProductName)
  boolean existsByCategory_CategoryIdAndProductNameIgnoreCase(Long categoryId, String productName);

  boolean existsByCategory_CategoryIdAndProductNameIgnoreCaseAndProductIdNot(
    Long categoryId, String productName, Long productId
  );

  // útil para filtros
  List<Product> findAllByCategory_CategoryIdAndStatus(Long categoryId, Status status);
}
