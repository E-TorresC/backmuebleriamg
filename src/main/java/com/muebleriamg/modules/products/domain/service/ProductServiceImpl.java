package com.muebleriamg.modules.products.domain.service;

import com.muebleriamg.modules.categories.domain.entity.Category;
import com.muebleriamg.modules.categories.domain.repository.CategoryRepository;
import com.muebleriamg.modules.products.domain.entity.Product;
import com.muebleriamg.modules.products.domain.repository.ProductRepository;
import com.muebleriamg.modules.products.web.dto.ProductCreateRequest;
import com.muebleriamg.modules.products.web.dto.ProductResponse;
import com.muebleriamg.modules.products.web.dto.ProductUpdateRequest;
import com.muebleriamg.modules.products.web.mapper.ProductMapper;
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
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;
  private final ProductMapper productMapper;

  @Override
  @Transactional
  public ProductResponse create(ProductCreateRequest request) {

    Category category = categoryRepository.findByCategoryIdAndStatus(request.getCategoryId(), Status.ACTIVE)
      .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada o inactiva."));

    if (productRepository.existsByCategory_CategoryIdAndProductNameIgnoreCase(
      request.getCategoryId(), request.getProductName()
    )) {
      throw new ResourceConflictException("Ya existe un producto con ese nombre en la categoría seleccionada.");
    }

    Product entity = productMapper.toEntity(request);
    entity.setCategory(category);
    entity.setStatus(Status.ACTIVE);
    entity.setProductImageUrl("https://res.cloudinary.com/do4l2xa3x/image/upload/v1766036923/logoMG_hoyl7r.png");

    Product saved = productRepository.save(entity);
    return productMapper.toResponse(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public ProductResponse getById(Long id) {

    Product entity = productRepository.findByProductIdAndStatus(id, Status.ACTIVE)
      .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado o inactivo."));

    return productMapper.toResponse(entity);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ProductResponse> getAllActive() {
    return productRepository.findAllByStatus(Status.ACTIVE)
      .stream()
      .map(productMapper::toResponse)
      .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<ProductResponse> getAllActiveByCategory(Long categoryId) {

    // Validación opcional: si desea exigir que exista la categoría
    categoryRepository.findByCategoryIdAndStatus(categoryId, Status.ACTIVE)
      .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada o inactiva."));

    return productRepository.findAllByCategory_CategoryIdAndStatus(categoryId, Status.ACTIVE)
      .stream()
      .map(productMapper::toResponse)
      .toList();
  }

  @Override
  @Transactional
  public ProductResponse update(Long id, ProductUpdateRequest request) {

    Product entity = productRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado."));

    if (entity.getStatus() != Status.ACTIVE) {
      throw new ResourceNotFoundException("No se puede actualizar: el producto no está activo.");
    }

    Category category = categoryRepository.findByCategoryIdAndStatus(request.getCategoryId(), Status.ACTIVE)
      .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada o inactiva."));

    if (productRepository.existsByCategory_CategoryIdAndProductNameIgnoreCaseAndProductIdNot(
      request.getCategoryId(), request.getProductName(), id
    )) {
      throw new ResourceConflictException("Ya existe otro producto con ese nombre en la categoría seleccionada.");
    }

    productMapper.updateEntityFromRequest(request, entity);
    entity.setCategory(category);

    Product saved = productRepository.save(entity);
    return productMapper.toResponse(saved);
  }

  @Override
  @Transactional
  public void deleteLogical(Long id) {

    Product entity = productRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado."));

    if (entity.getStatus() == Status.INACTIVE) {
      return; // idempotente
    }

    entity.setStatus(Status.INACTIVE);
    productRepository.save(entity);
  }

  @Override
  @Transactional
  public ProductResponse activate(Long id) {

    Product entity = productRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado."));

    if (entity.getStatus() == Status.BLOCKED) {
      throw new BusinessException("No se puede activar: el producto está bloqueado.");
    }

    entity.setStatus(Status.ACTIVE);
    Product saved = productRepository.save(entity);

    return productMapper.toResponse(saved);
  }
}
