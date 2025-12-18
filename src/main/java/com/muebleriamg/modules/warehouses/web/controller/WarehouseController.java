package com.muebleriamg.modules.warehouses.web.controller;

import com.muebleriamg.modules.warehouses.domain.service.WarehouseService;
import com.muebleriamg.modules.warehouses.web.dto.WarehouseCreateRequest;
import com.muebleriamg.modules.warehouses.web.dto.WarehouseResponse;
import com.muebleriamg.modules.warehouses.web.dto.WarehouseUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/warehouses")
@RequiredArgsConstructor
public class WarehouseController {

  private final WarehouseService warehouseService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public WarehouseResponse create(@Valid @RequestBody WarehouseCreateRequest request) {
    return warehouseService.create(request);
  }

  @GetMapping("/{id}")
  public WarehouseResponse getById(@PathVariable Long id) {
    return warehouseService.getById(id);
  }

  @GetMapping
  public List<WarehouseResponse> getAllActive() {
    return warehouseService.getAllActive();
  }

  @PutMapping("/{id}")
  public WarehouseResponse update(@PathVariable Long id,
                                  @Valid @RequestBody WarehouseUpdateRequest request) {
    return warehouseService.update(id, request);
  }

  /** Eliminación lógica: Status => INACTIVE */
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteLogical(@PathVariable Long id) {
    warehouseService.deleteLogical(id);
  }

  /** Reactivar: INACTIVE => ACTIVE (si no está BLOCKED) */
  @PatchMapping("/activate/{id}")
  public WarehouseResponse activate(@PathVariable Long id) {
    return warehouseService.activate(id);
  }
}
