package com.muebleriamg.modules.warehouses.domain.service;

import com.muebleriamg.modules.warehouses.domain.entity.Warehouse;
import com.muebleriamg.modules.warehouses.domain.repository.WarehouseRepository;
import com.muebleriamg.modules.warehouses.web.dto.WarehouseCreateRequest;
import com.muebleriamg.modules.warehouses.web.dto.WarehouseResponse;
import com.muebleriamg.modules.warehouses.web.dto.WarehouseUpdateRequest;
import com.muebleriamg.modules.warehouses.web.mapper.WarehouseMapper;
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
public class WarehouseServiceImpl implements WarehouseService {

  private final WarehouseRepository warehouseRepository;
  private final WarehouseMapper warehouseMapper;

  @Override
  @Transactional
  public WarehouseResponse create(WarehouseCreateRequest request) {

    if (warehouseRepository.existsByWarehouseNameIgnoreCase(request.getWarehouseName())) {
      throw new ResourceConflictException("Ya existe un almacén con ese nombre.");
    }
    if (warehouseRepository.existsByWarehouseSlugIgnoreCase(request.getWarehouseSlug())) {
      throw new ResourceConflictException("Ya existe un almacén con ese slug.");
    }

    Warehouse entity = warehouseMapper.toEntity(request);
    entity.setStatus(Status.ACTIVE);

    Warehouse saved = warehouseRepository.save(entity);
    return warehouseMapper.toResponse(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public WarehouseResponse getById(Long id) {
    Warehouse entity = warehouseRepository.findByWarehouseIdAndStatus(id, Status.ACTIVE)
      .orElseThrow(() -> new ResourceNotFoundException("Almacén no encontrado o inactivo."));

    return warehouseMapper.toResponse(entity);
  }

  @Override
  @Transactional(readOnly = true)
  public List<WarehouseResponse> getAllActive() {
    return warehouseRepository.findAllByStatus(Status.ACTIVE)
      .stream()
      .map(warehouseMapper::toResponse)
      .toList();
  }

  @Override
  @Transactional
  public WarehouseResponse update(Long id, WarehouseUpdateRequest request) {

    Warehouse entity = warehouseRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Almacén no encontrado."));

    if (entity.getStatus() != Status.ACTIVE) {
      throw new ResourceNotFoundException("No se puede actualizar: el almacén no está activo.");
    }

    if (warehouseRepository.existsByWarehouseNameIgnoreCaseAndWarehouseIdNot(request.getWarehouseName(), id)) {
      throw new ResourceConflictException("Ya existe otro almacén con ese nombre.");
    }
    if (warehouseRepository.existsByWarehouseSlugIgnoreCaseAndWarehouseIdNot(request.getWarehouseSlug(), id)) {
      throw new ResourceConflictException("Ya existe otro almacén con ese slug.");
    }

    warehouseMapper.updateEntityFromRequest(request, entity);
    Warehouse saved = warehouseRepository.save(entity);

    return warehouseMapper.toResponse(saved);
  }

  @Override
  @Transactional
  public void deleteLogical(Long id) {

    Warehouse entity = warehouseRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Almacén no encontrado."));

    if (entity.getStatus() == Status.INACTIVE) {
      return; // idempotente
    }

    entity.setStatus(Status.INACTIVE);
    warehouseRepository.save(entity);
  }

  @Override
  @Transactional
  public WarehouseResponse activate(Long id) {

    Warehouse entity = warehouseRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Almacén no encontrado."));

    if (entity.getStatus() == Status.BLOCKED) {
      throw new BusinessException("No se puede activar: el almacén está bloqueado.");
    }

    entity.setStatus(Status.ACTIVE);
    Warehouse saved = warehouseRepository.save(entity);

    return warehouseMapper.toResponse(saved);
  }
}
