package com.muebleriamg.modules.measures.domain.service;

import com.muebleriamg.modules.measures.web.dto.MeasureCreateRequest;
import com.muebleriamg.modules.measures.web.dto.MeasureResponse;
import com.muebleriamg.modules.measures.web.dto.MeasureUpdateRequest;

import java.util.List;

public interface MeasureService {

  MeasureResponse create(MeasureCreateRequest request);

  MeasureResponse getById(Long id);

  List<MeasureResponse> getAllActive();

  MeasureResponse update(Long id, MeasureUpdateRequest request);

  void deleteLogical(Long id);

  MeasureResponse activate(Long id);
}

