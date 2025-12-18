package com.muebleriamg.modules.productmeasures.domain.service;

import com.muebleriamg.modules.measures.domain.entity.Measure;
import com.muebleriamg.modules.measures.domain.repository.MeasureRepository;
import com.muebleriamg.modules.productmeasures.domain.entity.ProductMeasure;
import com.muebleriamg.modules.productmeasures.domain.repository.ProductMeasureRepository;
import com.muebleriamg.modules.productmeasures.web.dto.ProductMeasureCreateRequest;
import com.muebleriamg.modules.productmeasures.web.dto.ProductMeasureResponse;
import com.muebleriamg.modules.productmeasures.web.dto.ProductMeasureUpdateRequest;
import com.muebleriamg.modules.productmeasures.web.mapper.ProductMeasureMapper;
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
public class ProductMeasureServiceImpl implements ProductMeasureService {

  private final ProductMeasureRepository productMeasureRepository;
  private final ProductRepository productRepository;
  private final MeasureRepository measureRepository;
  private final ProductMeasureMapper productMeasureMapper;

  @Override
  @Transactional
  public ProductMeasureResponse create(ProductMeasureCreateRequest request) {

    Product product = productRepository.findByProductIdAndStatus(request.getProductId(), Status.ACTIVE)
      .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado o inactivo."));

    Measure measure = measureRepository.findByMeasureIdAndStatus(request.getMeasureId(), Status.ACTIVE)
      .orElseThrow(() -> new ResourceNotFoundException("Medida no encontrada o inactiva."));

    // Si ya existe el par (productId, measureId), por UQ no se puede insertar de nuevo.
    // Mejora: si está INACTIVE, se reactiva y se actualiza el precio.
    ProductMeasure existing = productMeasureRepository
      .findByProduct_ProductIdAndMeasure_MeasureId(request.getProductId(), request.getMeasureId())
      .orElse(null);

    if (existing != null) {
      if (existing.getStatus() == Status.BLOCKED) {
        throw new BusinessException("No se puede modificar: el precio por medida está bloqueado.");
      }
      existing.setProduct(product);
      existing.setMeasure(measure);
      existing.setPrice(request.getPrice());
      existing.setStatus(Status.ACTIVE);
      return productMeasureMapper.toResponse(productMeasureRepository.save(existing));
    }

    ProductMeasure entity = productMeasureMapper.toEntity(request);
    entity.setProduct(product);
    entity.setMeasure(measure);
    entity.setStatus(Status.ACTIVE);

    ProductMeasure saved = productMeasureRepository.save(entity);
    return productMeasureMapper.toResponse(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public ProductMeasureResponse getById(Long id) {

    ProductMeasure entity = productMeasureRepository.findByProductMeasureIdAndStatus(id, Status.ACTIVE)
      .orElseThrow(() -> new ResourceNotFoundException("Precio por medida no encontrado o inactivo."));

    return productMeasureMapper.toResponse(entity);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ProductMeasureResponse> getAllActive() {
    return productMeasureRepository.findAllByStatus(Status.ACTIVE)
      .stream()
      .map(productMeasureMapper::toResponse)
      .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<ProductMeasureResponse> getAllActiveByFilter(Long productId, Long measureId) {

    if (productId != null && measureId != null) {
      return productMeasureRepository
        .findAllByProduct_ProductIdAndMeasure_MeasureIdAndStatus(productId, measureId, Status.ACTIVE)
        .stream().map(productMeasureMapper::toResponse).toList();
    }

    if (productId != null) {
      return productMeasureRepository
        .findAllByProduct_ProductIdAndStatus(productId, Status.ACTIVE)
        .stream().map(productMeasureMapper::toResponse).toList();
    }

    if (measureId != null) {
      return productMeasureRepository
        .findAllByMeasure_MeasureIdAndStatus(measureId, Status.ACTIVE)
        .stream().map(productMeasureMapper::toResponse).toList();
    }

    return getAllActive();
  }

  @Override
  @Transactional
  public ProductMeasureResponse update(Long id, ProductMeasureUpdateRequest request) {

    ProductMeasure entity = productMeasureRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Precio por medida no encontrado."));

    if (entity.getStatus() != Status.ACTIVE) {
      throw new ResourceNotFoundException("No se puede actualizar: el registro no está activo.");
    }

    Product product = productRepository.findByProductIdAndStatus(request.getProductId(), Status.ACTIVE)
      .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado o inactivo."));

    Measure measure = measureRepository.findByMeasureIdAndStatus(request.getMeasureId(), Status.ACTIVE)
      .orElseThrow(() -> new ResourceNotFoundException("Medida no encontrada o inactiva."));

    if (productMeasureRepository.existsByProduct_ProductIdAndMeasure_MeasureIdAndProductMeasureIdNot(
      request.getProductId(), request.getMeasureId(), id
    )) {
      throw new ResourceConflictException("Ya existe otro registro para ese producto y esa medida.");
    }

    productMeasureMapper.updateEntityFromRequest(request, entity);
    entity.setProduct(product);
    entity.setMeasure(measure);

    ProductMeasure saved = productMeasureRepository.save(entity);
    return productMeasureMapper.toResponse(saved);
  }

  @Override
  @Transactional
  public void deleteLogical(Long id) {

    ProductMeasure entity = productMeasureRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Precio por medida no encontrado."));

    if (entity.getStatus() == Status.INACTIVE) {
      return;
    }

    entity.setStatus(Status.INACTIVE);
    productMeasureRepository.save(entity);
  }

  @Override
  @Transactional
  public ProductMeasureResponse activate(Long id) {

    ProductMeasure entity = productMeasureRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Precio por medida no encontrado."));

    if (entity.getStatus() == Status.BLOCKED) {
      throw new BusinessException("No se puede activar: el registro está bloqueado.");
    }

    entity.setStatus(Status.ACTIVE);
    ProductMeasure saved = productMeasureRepository.save(entity);

    return productMeasureMapper.toResponse(saved);
  }
}
