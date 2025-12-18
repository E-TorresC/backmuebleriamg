package com.muebleriamg.modules.measures.web.controller;

import com.muebleriamg.modules.measures.domain.service.MeasureService;
import com.muebleriamg.modules.measures.web.dto.MeasureCreateRequest;
import com.muebleriamg.modules.measures.web.dto.MeasureResponse;
import com.muebleriamg.modules.measures.web.dto.MeasureUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/measures")
@RequiredArgsConstructor
public class MeasureController {

  private final MeasureService measureService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public MeasureResponse create(@Valid @RequestBody MeasureCreateRequest request) {
    return measureService.create(request);
  }

  @GetMapping("/{id}")
  public MeasureResponse getById(@PathVariable Long id) {
    return measureService.getById(id);
  }

  @GetMapping
  public List<MeasureResponse> getAllActive() {
    return measureService.getAllActive();
  }

  @PutMapping("/{id}")
  public MeasureResponse update(@PathVariable Long id,
                                @Valid @RequestBody MeasureUpdateRequest request) {
    return measureService.update(id, request);
  }

  /** Eliminación lógica: Status => INACTIVE */
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteLogical(@PathVariable Long id) {
    measureService.deleteLogical(id);
  }

  /** Reactivar: INACTIVE => ACTIVE (si no está BLOCKED) */
  @PatchMapping("/activate/{id}")
  public MeasureResponse activate(@PathVariable Long id) {
    return measureService.activate(id);
  }
}

