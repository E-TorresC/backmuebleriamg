package com.muebleriamg.modules.stockbyvariant.domain.service;

import com.muebleriamg.modules.stockbyvariant.web.dto.*;

import java.util.List;

public interface StockByVariantService {

  StockResponse create(StockCreateRequest request);

  StockResponse getById(Long id);

  List<StockResponse> getAllActive();

  List<StockResponse> getAllActiveByFilter(Long productId, Long measureId, Long colorId, Long warehouseId);

  StockResponse update(Long id, StockUpdateRequest request);

  void deleteLogical(Long id);

  StockResponse activate(Long id);

  StockResponse setQuantity(Long id, StockSetQuantityRequest request);

  StockResponse adjustQuantity(Long id, StockAdjustQuantityRequest request);

  StockAvailabilityResponse getAvailability(Long productId, Long measureId, Long colorId);

  StockAvailabilityResponse getAvailabilityByWarehouse(Long productId, Long measureId, Long colorId, Long warehouseId);

}
