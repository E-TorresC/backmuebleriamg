package com.muebleriamg.modules.measures.domain.service;

import com.muebleriamg.modules.measures.domain.entity.Measure;
import com.muebleriamg.modules.measures.domain.repository.MeasureRepository;
import com.muebleriamg.modules.measures.web.dto.MeasureCreateRequest;
import com.muebleriamg.modules.measures.web.dto.MeasureResponse;
import com.muebleriamg.modules.measures.web.dto.MeasureUpdateRequest;
import com.muebleriamg.modules.measures.web.mapper.MeasureMapper;
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
public class MeasureServiceImpl implements MeasureService {

  private final MeasureRepository measureRepository;
  private final MeasureMapper measureMapper;

  @Override
  @Transactional
  public MeasureResponse create(MeasureCreateRequest request) {

    if (measureRepository.existsByWidthCmAndHeightCmAndDepthCm(
      request.getWidthCm(), request.getHeightCm(), request.getDepthCm()
    )) {
      throw new ResourceConflictException("Ya existe una medida con el mismo ancho, alto y profundidad.");
    }

    Measure entity = measureMapper.toEntity(request);
    entity.setStatus(Status.ACTIVE);

    Measure saved = measureRepository.save(entity);
    return measureMapper.toResponse(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public MeasureResponse getById(Long id) {

    Measure entity = measureRepository.findByMeasureIdAndStatus(id, Status.ACTIVE)
      .orElseThrow(() -> new ResourceNotFoundException("Medida no encontrada o inactiva."));

    return measureMapper.toResponse(entity);
  }

  @Override
  @Transactional(readOnly = true)
  public List<MeasureResponse> getAllActive() {
    return measureRepository.findAllByStatus(Status.ACTIVE)
      .stream()
      .map(measureMapper::toResponse)
      .toList();
  }

  @Override
  @Transactional
  public MeasureResponse update(Long id, MeasureUpdateRequest request) {

    Measure entity = measureRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Medida no encontrada."));

    if (entity.getStatus() != Status.ACTIVE) {
      throw new ResourceNotFoundException("No se puede actualizar: la medida no está activa.");
    }

    if (measureRepository.existsByWidthCmAndHeightCmAndDepthCmAndMeasureIdNot(
      request.getWidthCm(), request.getHeightCm(), request.getDepthCm(), id
    )) {
      throw new ResourceConflictException("Ya existe otra medida con el mismo ancho, alto y profundidad.");
    }

    measureMapper.updateEntityFromRequest(request, entity);
    Measure saved = measureRepository.save(entity);

    return measureMapper.toResponse(saved);
  }

  @Override
  @Transactional
  public void deleteLogical(Long id) {

    Measure entity = measureRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Medida no encontrada."));

    if (entity.getStatus() == Status.INACTIVE) {
      return; // idempotente
    }

    entity.setStatus(Status.INACTIVE);
    measureRepository.save(entity);
  }

  @Override
  @Transactional
  public MeasureResponse activate(Long id) {

    Measure entity = measureRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Medida no encontrada."));

    if (entity.getStatus() == Status.BLOCKED) {
      throw new BusinessException("No se puede activar: la medida está bloqueada.");
    }

    entity.setStatus(Status.ACTIVE);
    Measure saved = measureRepository.save(entity);

    return measureMapper.toResponse(saved);
  }
}

