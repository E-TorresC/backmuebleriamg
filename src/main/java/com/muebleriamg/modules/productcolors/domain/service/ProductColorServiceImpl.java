package com.muebleriamg.modules.productcolors.domain.service;

import com.muebleriamg.modules.colors.domain.entity.Color;
import com.muebleriamg.modules.colors.domain.repository.ColorRepository;
import com.muebleriamg.modules.productcolors.domain.entity.ProductColor;
import com.muebleriamg.modules.productcolors.domain.repository.ProductColorRepository;
import com.muebleriamg.modules.productcolors.web.dto.ProductColorCreateRequest;
import com.muebleriamg.modules.productcolors.web.dto.ProductColorResponse;
import com.muebleriamg.modules.productcolors.web.dto.ProductColorUpdateRequest;
import com.muebleriamg.modules.productcolors.web.mapper.ProductColorMapper;
import com.muebleriamg.modules.products.domain.entity.Product;
import com.muebleriamg.modules.products.domain.repository.ProductRepository;
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
public class ProductColorServiceImpl implements ProductColorService {

  private final ProductColorRepository productColorRepository;
  private final ProductRepository productRepository;
  private final ColorRepository colorRepository;
  private final ProductColorMapper productColorMapper;

  @Override
  @Transactional
  public ProductColorResponse create(ProductColorCreateRequest request) {

    Product product = productRepository.findByProductIdAndStatus(request.getProductId(), Status.ACTIVE)
      .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado o inactivo."));

    Color color = colorRepository.findByColorIdAndStatus(request.getColorId(), Status.ACTIVE)
      .orElseThrow(() -> new ResourceNotFoundException("Color no encontrado o inactivo."));

    // Mejora: si ya existe el par (productId, colorId), se reactiva (si estaba INACTIVE)
    ProductColor existing = productColorRepository
      .findByProduct_ProductIdAndColor_ColorId(request.getProductId(), request.getColorId())
      .orElse(null);

    if (existing != null) {
      if (existing.getStatus() == Status.BLOCKED) {
        throw new BusinessException("No se puede modificar: la relación producto-color está bloqueada.");
      }
      existing.setProduct(product);
      existing.setColor(color);
      existing.setStatus(Status.ACTIVE);
      return productColorMapper.toResponse(productColorRepository.save(existing));
    }

    ProductColor entity = productColorMapper.toEntity(request);
    entity.setProduct(product);
    entity.setColor(color);
    entity.setStatus(Status.ACTIVE);

    ProductColor saved = productColorRepository.save(entity);
    return productColorMapper.toResponse(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public ProductColorResponse getById(Long id) {

    ProductColor entity = productColorRepository.findByProductColorIdAndStatus(id, Status.ACTIVE)
      .orElseThrow(() -> new ResourceNotFoundException("Relación producto-color no encontrada o inactiva."));

    return productColorMapper.toResponse(entity);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ProductColorResponse> getAllActive() {
    return productColorRepository.findAllByStatus(Status.ACTIVE)
      .stream()
      .map(productColorMapper::toResponse)
      .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<ProductColorResponse> getAllActiveByFilter(Long productId, Long colorId) {

    if (productId != null && colorId != null) {
      return productColorRepository
        .findAllByProduct_ProductIdAndColor_ColorIdAndStatus(productId, colorId, Status.ACTIVE)
        .stream().map(productColorMapper::toResponse).toList();
    }

    if (productId != null) {
      return productColorRepository
        .findAllByProduct_ProductIdAndStatus(productId, Status.ACTIVE)
        .stream().map(productColorMapper::toResponse).toList();
    }

    if (colorId != null) {
      return productColorRepository
        .findAllByColor_ColorIdAndStatus(colorId, Status.ACTIVE)
        .stream().map(productColorMapper::toResponse).toList();
    }

    return getAllActive();
  }

  @Override
  @Transactional
  public ProductColorResponse update(Long id, ProductColorUpdateRequest request) {

    ProductColor entity = productColorRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Relación producto-color no encontrada."));

    if (entity.getStatus() != Status.ACTIVE) {
      throw new ResourceNotFoundException("No se puede actualizar: el registro no está activo.");
    }

    Product product = productRepository.findByProductIdAndStatus(request.getProductId(), Status.ACTIVE)
      .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado o inactivo."));

    Color color = colorRepository.findByColorIdAndStatus(request.getColorId(), Status.ACTIVE)
      .orElseThrow(() -> new ResourceNotFoundException("Color no encontrado o inactivo."));

    if (productColorRepository.existsByProduct_ProductIdAndColor_ColorIdAndProductColorIdNot(
      request.getProductId(), request.getColorId(), id
    )) {
      throw new ResourceConflictException("Ya existe otra relación con ese producto y ese color.");
    }

    productColorMapper.updateEntityFromRequest(request, entity);
    entity.setProduct(product);
    entity.setColor(color);

    ProductColor saved = productColorRepository.save(entity);
    return productColorMapper.toResponse(saved);
  }

  @Override
  @Transactional
  public void deleteLogical(Long id) {

    ProductColor entity = productColorRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Relación producto-color no encontrada."));

    if (entity.getStatus() == Status.INACTIVE) {
      return;
    }

    entity.setStatus(Status.INACTIVE);
    productColorRepository.save(entity);
  }

  @Override
  @Transactional
  public ProductColorResponse activate(Long id) {

    ProductColor entity = productColorRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Relación producto-color no encontrada."));

    if (entity.getStatus() == Status.BLOCKED) {
      throw new BusinessException("No se puede activar: el registro está bloqueado.");
    }

    entity.setStatus(Status.ACTIVE);
    ProductColor saved = productColorRepository.save(entity);

    return productColorMapper.toResponse(saved);
  }
}
