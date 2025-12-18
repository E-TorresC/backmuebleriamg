package com.muebleriamg.modules.colors.web.controller;

import com.muebleriamg.modules.colors.domain.service.ColorService;
import com.muebleriamg.modules.colors.web.dto.ColorCreateRequest;
import com.muebleriamg.modules.colors.web.dto.ColorResponse;
import com.muebleriamg.modules.colors.web.dto.ColorUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/colors")
@RequiredArgsConstructor
public class ColorController {

  private final ColorService colorService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ColorResponse create(@Valid @RequestBody ColorCreateRequest request) {
    return colorService.create(request);
  }

  @GetMapping("/{id}")
  public ColorResponse getById(@PathVariable Long id) {
    return colorService.getById(id);
  }

  @GetMapping
  public List<ColorResponse> getAllActive() {
    return colorService.getAllActive();
  }

  @PutMapping("/{id}")
  public ColorResponse update(@PathVariable Long id,
                              @Valid @RequestBody ColorUpdateRequest request) {
    return colorService.update(id, request);
  }

  /** Eliminación lógica: Status => INACTIVE */
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteLogical(@PathVariable Long id) {
    colorService.deleteLogical(id);
  }

  /** Reactivar: INACTIVE => ACTIVE (si no está BLOCKED) */
  @PatchMapping("/activate/{id}")
  public ColorResponse activate(@PathVariable Long id) {
    return colorService.activate(id);
  }
}
