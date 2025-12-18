package com.muebleriamg.modules.categories.domain.service;


import com.muebleriamg.modules.categories.web.dto.CategoryCreateRequest;
import com.muebleriamg.modules.categories.web.dto.CategoryResponse;
import com.muebleriamg.modules.categories.web.dto.CategoryUpdateRequest;

import java.util.List;

public interface CategoryService {

  CategoryResponse create(CategoryCreateRequest request);

  CategoryResponse getById(Long id);

  List<CategoryResponse> getAllActive();

  CategoryResponse update(Long id, CategoryUpdateRequest request);

  void deleteLogical(Long id);

  CategoryResponse activate(Long id);

}

