package com.muebleriamg.modules.categories.domain.repository;

import com.muebleriamg.modules.categories.domain.entity.Category;
import com.muebleriamg.shared.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

  List<Category> findAllByStatus(Status status);

  Optional<Category> findByCategoryIdAndStatus(Long categoryId, Status status);

  boolean existsByCategoryNameIgnoreCase(String categoryName);

  boolean existsByCategorySlugIgnoreCase(String categorySlug);

  boolean existsByCategoryNameIgnoreCaseAndCategoryIdNot(String categoryName, Long categoryId);

  boolean existsByCategorySlugIgnoreCaseAndCategoryIdNot(String categorySlug, Long categoryId);
}

