package com.muebleriamg.modules.productcolors.domain.service;

import com.muebleriamg.modules.productcolors.web.dto.ProductColorCreateRequest;
import com.muebleriamg.modules.productcolors.web.dto.ProductColorResponse;
import com.muebleriamg.modules.productcolors.web.dto.ProductColorUpdateRequest;

import java.util.List;

public interface ProductColorService {

  ProductColorResponse create(ProductColorCreateRequest request);

  ProductColorResponse getById(Long id);

  List<ProductColorResponse> getAllActive();

  List<ProductColorResponse> getAllActiveByFilter(Long productId, Long colorId);

  ProductColorResponse update(Long id, ProductColorUpdateRequest request);

  void deleteLogical(Long id);

  ProductColorResponse activate(Long id);
}
