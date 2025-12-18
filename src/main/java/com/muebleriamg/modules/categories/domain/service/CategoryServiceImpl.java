package com.muebleriamg.modules.categories.domain.service;

import com.muebleriamg.modules.categories.domain.entity.Category;
import com.muebleriamg.modules.categories.domain.repository.CategoryRepository;
import com.muebleriamg.modules.categories.web.dto.CategoryCreateRequest;
import com.muebleriamg.modules.categories.web.dto.CategoryResponse;
import com.muebleriamg.modules.categories.web.dto.CategoryUpdateRequest;

import com.muebleriamg.modules.categories.web.mapper.CategoryMapper;
import com.muebleriamg.shared.enums.Status;
import com.muebleriamg.shared.exceptions.BusinessException;
import com.muebleriamg.shared.exceptions.ResourceConflictException;
import com.muebleriamg.shared.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository categoryRepository;
  private final CategoryMapper categoryMapper;

  @Override
  @Transactional
  public CategoryResponse create(CategoryCreateRequest request) {

    if (categoryRepository.existsByCategoryNameIgnoreCase(request.getCategoryName())) {
      throw new ResourceConflictException("Ya existe una categoría con ese nombre.");
    }
    if (categoryRepository.existsByCategorySlugIgnoreCase(request.getCategorySlug())) {
      throw new ResourceConflictException("Ya existe una categoría con ese slug.");
    }

    Category entity = categoryMapper.toEntity(request);
    entity.setStatus(Status.ACTIVE);

    Category saved = categoryRepository.save(entity);
    return categoryMapper.toResponse(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public CategoryResponse getById(Long id) {
    Category entity = categoryRepository.findByCategoryIdAndStatus(id, Status.ACTIVE)
      .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada o inactiva."));

    return categoryMapper.toResponse(entity);
  }

  @Override
  @Transactional(readOnly = true)
  public List<CategoryResponse> getAllActive() {
    return categoryRepository.findAllByStatus(Status.ACTIVE)
      .stream()
      .map(categoryMapper::toResponse)
      .toList();
  }

  @Override
  @Transactional
  public CategoryResponse update(Long id, CategoryUpdateRequest request) {

    Category entity = categoryRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada."));

    if (entity.getStatus() != Status.ACTIVE) {
      throw new ResourceNotFoundException("No se puede actualizar: la categoría no está activa.");
    }

    if (categoryRepository.existsByCategoryNameIgnoreCaseAndCategoryIdNot(request.getCategoryName(), id)) {
      throw new ResourceConflictException("Ya existe otra categoría con ese nombre.");
    }
    if (categoryRepository.existsByCategorySlugIgnoreCaseAndCategoryIdNot(request.getCategorySlug(), id)) {
      throw new ResourceConflictException("Ya existe otra categoría con ese slug.");
    }

    categoryMapper.updateEntityFromRequest(request, entity);
    Category saved = categoryRepository.save(entity);

    return categoryMapper.toResponse(saved);
  }

  @Override
  @Transactional
  public void deleteLogical(Long id) {

    Category entity = categoryRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada."));

    if (entity.getStatus() == Status.INACTIVE) {
      return; // idempotente
    }

    entity.setStatus(Status.INACTIVE);
    categoryRepository.save(entity);
  }

  @Override
  @Transactional
  public CategoryResponse activate(Long id) {

    Category entity = categoryRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada."));

    if (entity.getStatus() == Status.BLOCKED) {
      throw new BusinessException("No se puede activar: la categoría está bloqueada.");
    }

    entity.setStatus(Status.ACTIVE);
    Category saved = categoryRepository.save(entity);

    return categoryMapper.toResponse(saved);
  }

}

