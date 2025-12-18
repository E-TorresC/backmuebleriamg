package com.muebleriamg.modules.colors.domain.service;

import com.muebleriamg.modules.colors.web.dto.ColorCreateRequest;
import com.muebleriamg.modules.colors.web.dto.ColorResponse;
import com.muebleriamg.modules.colors.web.dto.ColorUpdateRequest;

import java.util.List;

public interface ColorService {

  ColorResponse create(ColorCreateRequest request);

  ColorResponse getById(Long id);

  List<ColorResponse> getAllActive();

  ColorResponse update(Long id, ColorUpdateRequest request);

  void deleteLogical(Long id);

  ColorResponse activate(Long id);
}
