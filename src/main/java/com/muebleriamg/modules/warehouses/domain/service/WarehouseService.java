package com.muebleriamg.modules.warehouses.domain.service;

import com.muebleriamg.modules.warehouses.web.dto.WarehouseCreateRequest;
import com.muebleriamg.modules.warehouses.web.dto.WarehouseResponse;
import com.muebleriamg.modules.warehouses.web.dto.WarehouseUpdateRequest;

import java.util.List;

public interface WarehouseService {

  WarehouseResponse create(WarehouseCreateRequest request);

  WarehouseResponse getById(Long id);

  List<WarehouseResponse> getAllActive();

  WarehouseResponse update(Long id, WarehouseUpdateRequest request);

  void deleteLogical(Long id);

  WarehouseResponse activate(Long id);
}
