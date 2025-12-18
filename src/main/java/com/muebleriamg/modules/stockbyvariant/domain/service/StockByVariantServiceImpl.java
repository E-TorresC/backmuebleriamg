package com.muebleriamg.modules.stockbyvariant.domain.service;

import com.muebleriamg.modules.colors.domain.entity.Color;
import com.muebleriamg.modules.colors.domain.repository.ColorRepository;
import com.muebleriamg.modules.measures.domain.entity.Measure;
import com.muebleriamg.modules.measures.domain.repository.MeasureRepository;
import com.muebleriamg.modules.productcolors.domain.repository.ProductColorRepository;
import com.muebleriamg.modules.productmeasures.domain.repository.ProductMeasureRepository;
import com.muebleriamg.modules.products.domain.entity.Product;
import com.muebleriamg.modules.products.domain.repository.ProductRepository;
import com.muebleriamg.modules.stockbyvariant.domain.entity.StockByVariant;
import com.muebleriamg.modules.stockbyvariant.domain.repository.StockByVariantRepository;
import com.muebleriamg.modules.stockbyvariant.web.dto.*;
import com.muebleriamg.modules.stockbyvariant.web.mapper.StockByVariantMapper;
import com.muebleriamg.modules.warehouses.domain.entity.Warehouse;
import com.muebleriamg.modules.warehouses.domain.repository.WarehouseRepository;
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
public class StockByVariantServiceImpl implements StockByVariantService {

  private final StockByVariantRepository stockRepository;

  private final ProductRepository productRepository;
  private final MeasureRepository measureRepository;
  private final ColorRepository colorRepository;
  private final WarehouseRepository warehouseRepository;

  private final ProductMeasureRepository productMeasureRepository;
  private final ProductColorRepository productColorRepository;

  private final StockByVariantMapper stockMapper;

  @Override
  @Transactional
  public StockResponse create(StockCreateRequest request) {

    if (request.getQuantity() < 0) {
      throw new BusinessException("La cantidad no puede ser negativa.");
    }

    Product product = productRepository.findByProductIdAndStatus(request.getProductId(), Status.ACTIVE)
      .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado o inactivo."));

    Measure measure = measureRepository.findByMeasureIdAndStatus(request.getMeasureId(), Status.ACTIVE)
      .orElseThrow(() -> new ResourceNotFoundException("Medida no encontrada o inactiva."));

    Color color = colorRepository.findByColorIdAndStatus(request.getColorId(), Status.ACTIVE)
      .orElseThrow(() -> new ResourceNotFoundException("Color no encontrado o inactivo."));

    Warehouse warehouse = warehouseRepository.findByWarehouseIdAndStatus(request.getWarehouseId(), Status.ACTIVE)
      .orElseThrow(() -> new ResourceNotFoundException("Almacén no encontrado o inactivo."));

    // Regla: combinación válida (ProductMeasures + ProductColors)
    if (!productMeasureRepository.existsByProduct_ProductIdAndMeasure_MeasureIdAndStatus(
      request.getProductId(), request.getMeasureId(), Status.ACTIVE
    )) {
      throw new BusinessException("Combinación inválida: el producto no tiene asociada esa medida (ProductMeasures).");
    }

    if (!productColorRepository.existsByProduct_ProductIdAndColor_ColorIdAndStatus(
      request.getProductId(), request.getColorId(), Status.ACTIVE
    )) {
      throw new BusinessException("Combinación inválida: el producto no tiene asociado ese color (ProductColors).");
    }

    // Upsert inteligente por llave natural (UQ_Stock)
    StockByVariant existing = stockRepository
      .findByProduct_ProductIdAndMeasure_MeasureIdAndColor_ColorIdAndWarehouse_WarehouseId(
        request.getProductId(), request.getMeasureId(), request.getColorId(), request.getWarehouseId()
      )
      .orElse(null);

    if (existing != null) {
      if (existing.getStatus() == Status.BLOCKED) {
        throw new BusinessException("No se puede modificar: el stock está bloqueado.");
      }
      existing.setProduct(product);
      existing.setMeasure(measure);
      existing.setColor(color);
      existing.setWarehouse(warehouse);
      existing.setQuantity(request.getQuantity());
      existing.setStatus(Status.ACTIVE);
      return stockMapper.toResponse(stockRepository.save(existing));
    }

    StockByVariant entity = stockMapper.toEntity(request);
    entity.setProduct(product);
    entity.setMeasure(measure);
    entity.setColor(color);
    entity.setWarehouse(warehouse);
    entity.setQuantity(request.getQuantity());
    entity.setStatus(Status.ACTIVE);

    return stockMapper.toResponse(stockRepository.save(entity));
  }

  @Override
  @Transactional(readOnly = true)
  public StockResponse getById(Long id) {
    StockByVariant entity = stockRepository.findByStockIdAndStatus(id, Status.ACTIVE)
      .orElseThrow(() -> new ResourceNotFoundException("Stock no encontrado o inactivo."));
    return stockMapper.toResponse(entity);
  }

  @Override
  @Transactional(readOnly = true)
  public List<StockResponse> getAllActive() {
    return stockRepository.findAllByStatus(Status.ACTIVE)
      .stream().map(stockMapper::toResponse).toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<StockResponse> getAllActiveByFilter(Long productId, Long measureId, Long colorId, Long warehouseId) {

    if (productId != null && measureId != null && colorId != null && warehouseId != null) {
      return stockRepository
        .findAllByProduct_ProductIdAndMeasure_MeasureIdAndColor_ColorIdAndWarehouse_WarehouseIdAndStatus(
          productId, measureId, colorId, warehouseId, Status.ACTIVE
        )
        .stream().map(stockMapper::toResponse).toList();
    }

    if (productId != null && warehouseId != null) {
      return stockRepository.findAllByProduct_ProductIdAndWarehouse_WarehouseIdAndStatus(productId, warehouseId, Status.ACTIVE)
        .stream().map(stockMapper::toResponse).toList();
    }

    if (warehouseId != null) {
      return stockRepository.findAllByWarehouse_WarehouseIdAndStatus(warehouseId, Status.ACTIVE)
        .stream().map(stockMapper::toResponse).toList();
    }

    if (productId != null) {
      return stockRepository.findAllByProduct_ProductIdAndStatus(productId, Status.ACTIVE)
        .stream().map(stockMapper::toResponse).toList();
    }

    return getAllActive();
  }

  @Override
  @Transactional
  public StockResponse update(Long id, StockUpdateRequest request) {

    if (request.getQuantity() < 0) {
      throw new BusinessException("La cantidad no puede ser negativa.");
    }

    StockByVariant entity = stockRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Stock no encontrado."));

    if (entity.getStatus() != Status.ACTIVE) {
      throw new ResourceNotFoundException("No se puede actualizar: el stock no está activo.");
    }

    Product product = productRepository.findByProductIdAndStatus(request.getProductId(), Status.ACTIVE)
      .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado o inactivo."));

    Measure measure = measureRepository.findByMeasureIdAndStatus(request.getMeasureId(), Status.ACTIVE)
      .orElseThrow(() -> new ResourceNotFoundException("Medida no encontrada o inactiva."));

    Color color = colorRepository.findByColorIdAndStatus(request.getColorId(), Status.ACTIVE)
      .orElseThrow(() -> new ResourceNotFoundException("Color no encontrado o inactivo."));

    Warehouse warehouse = warehouseRepository.findByWarehouseIdAndStatus(request.getWarehouseId(), Status.ACTIVE)
      .orElseThrow(() -> new ResourceNotFoundException("Almacén no encontrado o inactivo."));

    // regla de combinación válida
    if (!productMeasureRepository.existsByProduct_ProductIdAndMeasure_MeasureIdAndStatus(request.getProductId(), request.getMeasureId(), Status.ACTIVE)) {
      throw new BusinessException("Combinación inválida: el producto no tiene asociada esa medida (ProductMeasures).");
    }
    if (!productColorRepository.existsByProduct_ProductIdAndColor_ColorIdAndStatus(request.getProductId(), request.getColorId(), Status.ACTIVE)) {
      throw new BusinessException("Combinación inválida: el producto no tiene asociado ese color (ProductColors).");
    }

    // evitar duplicidad si cambian llaves
    if (stockRepository.existsByProduct_ProductIdAndMeasure_MeasureIdAndColor_ColorIdAndWarehouse_WarehouseIdAndStockIdNot(
      request.getProductId(), request.getMeasureId(), request.getColorId(), request.getWarehouseId(), id
    )) {
      throw new ResourceConflictException("Ya existe stock para esa variante en ese almacén.");
    }

    stockMapper.updateEntityFromRequest(request, entity);
    entity.setProduct(product);
    entity.setMeasure(measure);
    entity.setColor(color);
    entity.setWarehouse(warehouse);
    entity.setQuantity(request.getQuantity());

    return stockMapper.toResponse(stockRepository.save(entity));
  }

  @Override
  @Transactional
  public void deleteLogical(Long id) {
    StockByVariant entity = stockRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Stock no encontrado."));

    if (entity.getStatus() == Status.INACTIVE) {
      return;
    }

    entity.setStatus(Status.INACTIVE);
    stockRepository.save(entity);
  }

  @Override
  @Transactional
  public StockResponse activate(Long id) {
    StockByVariant entity = stockRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Stock no encontrado."));

    if (entity.getStatus() == Status.BLOCKED) {
      throw new BusinessException("No se puede activar: el stock está bloqueado.");
    }

    entity.setStatus(Status.ACTIVE);
    return stockMapper.toResponse(stockRepository.save(entity));
  }

  @Override
  @Transactional
  public StockResponse setQuantity(Long id, StockSetQuantityRequest request) {

    if (request.getQuantity() < 0) {
      throw new BusinessException("La cantidad no puede ser negativa.");
    }

    StockByVariant entity = stockRepository.findByIdForUpdate(id)
      .orElseThrow(() -> new ResourceNotFoundException("Stock no encontrado."));

    if (entity.getStatus() != Status.ACTIVE) {
      throw new ResourceNotFoundException("No se puede actualizar: el stock no está activo.");
    }
    if (entity.getStatus() == Status.BLOCKED) {
      throw new BusinessException("No se puede modificar: el stock está bloqueado.");
    }

    entity.setQuantity(request.getQuantity());
    return stockMapper.toResponse(stockRepository.save(entity));
  }

  @Override
  @Transactional
  public StockResponse adjustQuantity(Long id, StockAdjustQuantityRequest request) {

    StockByVariant entity = stockRepository.findByIdForUpdate(id)
      .orElseThrow(() -> new ResourceNotFoundException("Stock no encontrado."));

    if (entity.getStatus() != Status.ACTIVE) {
      throw new ResourceNotFoundException("No se puede actualizar: el stock no está activo.");
    }
    if (entity.getStatus() == Status.BLOCKED) {
      throw new BusinessException("No se puede modificar: el stock está bloqueado.");
    }

    int newQty = entity.getQuantity() + request.getDelta();
    if (newQty < 0) {
      throw new BusinessException("Stock insuficiente: la operación dejaría la cantidad en negativo.");
    }

    entity.setQuantity(newQty);
    return stockMapper.toResponse(stockRepository.save(entity));
  }

  @Override
  @Transactional(readOnly = true)
  public StockAvailabilityResponse getAvailability(Long productId, Long measureId, Long colorId) {

    productRepository.findByProductIdAndStatus(productId, Status.ACTIVE)
      .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado o inactivo."));

    measureRepository.findByMeasureIdAndStatus(measureId, Status.ACTIVE)
      .orElseThrow(() -> new ResourceNotFoundException("Medida no encontrada o inactiva."));

    colorRepository.findByColorIdAndStatus(colorId, Status.ACTIVE)
      .orElseThrow(() -> new ResourceNotFoundException("Color no encontrado o inactivo."));

    if (!productMeasureRepository.existsByProduct_ProductIdAndMeasure_MeasureIdAndStatus(productId, measureId, Status.ACTIVE)) {
      throw new BusinessException("Combinación inválida: el producto no tiene asociada esa medida.");
    }

    if (!productColorRepository.existsByProduct_ProductIdAndColor_ColorIdAndStatus(productId, colorId, Status.ACTIVE)) {
      throw new BusinessException("Combinación inválida: el producto no tiene asociado ese color.");
    }

    long available = stockRepository.sumAvailableStockAllActiveWarehouses(productId, measureId, colorId);

    return StockAvailabilityResponse.builder()
      .productId(productId)
      .measureId(measureId)
      .colorId(colorId)
      .warehouseId(null)
      .available(available)
      .build();
  }

  @Override
  @Transactional(readOnly = true)
  public StockAvailabilityResponse getAvailabilityByWarehouse(Long productId, Long measureId, Long colorId, Long warehouseId) {

    productRepository.findByProductIdAndStatus(productId, Status.ACTIVE)
      .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado o inactivo."));

    measureRepository.findByMeasureIdAndStatus(measureId, Status.ACTIVE)
      .orElseThrow(() -> new ResourceNotFoundException("Medida no encontrada o inactiva."));

    colorRepository.findByColorIdAndStatus(colorId, Status.ACTIVE)
      .orElseThrow(() -> new ResourceNotFoundException("Color no encontrado o inactivo."));

    warehouseRepository.findByWarehouseIdAndStatus(warehouseId, Status.ACTIVE)
      .orElseThrow(() -> new ResourceNotFoundException("Almacén no encontrado o inactivo."));

    if (!productMeasureRepository.existsByProduct_ProductIdAndMeasure_MeasureIdAndStatus(productId, measureId, Status.ACTIVE)) {
      throw new BusinessException("Combinación inválida: el producto no tiene asociada esa medida.");
    }

    if (!productColorRepository.existsByProduct_ProductIdAndColor_ColorIdAndStatus(productId, colorId, Status.ACTIVE)) {
      throw new BusinessException("Combinación inválida: el producto no tiene asociado ese color.");
    }

    long available = stockRepository.sumAvailableStockByWarehouse(productId, measureId, colorId, warehouseId);

    return StockAvailabilityResponse.builder()
      .productId(productId)
      .measureId(measureId)
      .colorId(colorId)
      .warehouseId(warehouseId)
      .available(available)
      .build();
  }


}
