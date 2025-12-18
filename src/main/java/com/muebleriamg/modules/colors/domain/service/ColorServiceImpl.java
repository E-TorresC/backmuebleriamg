package com.muebleriamg.modules.colors.domain.service;

import com.muebleriamg.modules.colors.domain.entity.Color;
import com.muebleriamg.modules.colors.domain.repository.ColorRepository;
import com.muebleriamg.modules.colors.web.dto.ColorCreateRequest;
import com.muebleriamg.modules.colors.web.dto.ColorResponse;
import com.muebleriamg.modules.colors.web.dto.ColorUpdateRequest;
import com.muebleriamg.modules.colors.web.mapper.ColorMapper;
import com.muebleriamg.shared.enums.Status;
import com.muebleriamg.shared.exceptions.BusinessException;
import com.muebleriamg.shared.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ColorServiceImpl implements ColorService {

  private final ColorRepository colorRepository;
  private final ColorMapper colorMapper;

  @Override
  @Transactional
  public ColorResponse create(ColorCreateRequest request) {

    Color entity = colorMapper.toEntity(request);
    entity.setStatus(Status.ACTIVE);

    Color saved = colorRepository.save(entity);
    return colorMapper.toResponse(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public ColorResponse getById(Long id) {

    Color entity = colorRepository.findByColorIdAndStatus(id, Status.ACTIVE)
      .orElseThrow(() -> new ResourceNotFoundException("Color no encontrado o inactivo."));

    return colorMapper.toResponse(entity);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ColorResponse> getAllActive() {
    return colorRepository.findAllByStatus(Status.ACTIVE)
      .stream()
      .map(colorMapper::toResponse)
      .toList();
  }

  @Override
  @Transactional
  public ColorResponse update(Long id, ColorUpdateRequest request) {

    Color entity = colorRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Color no encontrado."));

    if (entity.getStatus() != Status.ACTIVE) {
      throw new ResourceNotFoundException("No se puede actualizar: el color no está activo.");
    }

    colorMapper.updateEntityFromRequest(request, entity);
    Color saved = colorRepository.save(entity);

    return colorMapper.toResponse(saved);
  }

  @Override
  @Transactional
  public void deleteLogical(Long id) {

    Color entity = colorRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Color no encontrado."));

    if (entity.getStatus() == Status.INACTIVE) {
      return; // idempotente
    }

    entity.setStatus(Status.INACTIVE);
    colorRepository.save(entity);
  }

  @Override
  @Transactional
  public ColorResponse activate(Long id) {

    Color entity = colorRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Color no encontrado."));

    if (entity.getStatus() == Status.BLOCKED) {
      throw new BusinessException("No se puede activar: el color está bloqueado.");
    }

    entity.setStatus(Status.ACTIVE);
    Color saved = colorRepository.save(entity);

    return colorMapper.toResponse(saved);
  }
}
