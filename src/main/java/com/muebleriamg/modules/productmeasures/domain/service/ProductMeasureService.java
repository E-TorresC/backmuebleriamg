package com.muebleriamg.modules.productmeasures.domain.service;

import com.muebleriamg.modules.productmeasures.web.dto.ProductMeasureCreateRequest;
import com.muebleriamg.modules.productmeasures.web.dto.ProductMeasureResponse;
import com.muebleriamg.modules.productmeasures.web.dto.ProductMeasureUpdateRequest;

import java.util.List;

public interface ProductMeasureService {

  ProductMeasureResponse create(ProductMeasureCreateRequest request);

  ProductMeasureResponse getById(Long id);

  List<ProductMeasureResponse> getAllActive();

  List<ProductMeasureResponse> getAllActiveByFilter(Long productId, Long measureId);

  ProductMeasureResponse update(Long id, ProductMeasureUpdateRequest request);

  void deleteLogical(Long id);

  ProductMeasureResponse activate(Long id);
}
