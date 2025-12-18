package com.muebleriamg.modules.stockbyvariant.web.controller;

import com.muebleriamg.modules.stockbyvariant.domain.service.StockByVariantService;
import com.muebleriamg.modules.stockbyvariant.web.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stock")
@RequiredArgsConstructor
public class StockByVariantController {

  private final StockByVariantService stockService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public StockResponse create(@Valid @RequestBody StockCreateRequest request) {
    return stockService.create(request);
  }

  @GetMapping("/{id}")
  public StockResponse getById(@PathVariable Long id) {
    return stockService.getById(id);
  }

  /**
   * Filtros opcionales:
   * - GET /api/v1/stock?warehouseId=1
   * - GET /api/v1/stock?productId=1
   * - GET /api/v1/stock?productId=1&warehouseId=1
   * - GET /api/v1/stock?productId=1&measureId=2&colorId=3&warehouseId=1
   */
  @GetMapping
  public List<StockResponse> getAllActive(
    @RequestParam(required = false) Long productId,
    @RequestParam(required = false) Long measureId,
    @RequestParam(required = false) Long colorId,
    @RequestParam(required = false) Long warehouseId
  ) {
    return stockService.getAllActiveByFilter(productId, measureId, colorId, warehouseId);
  }

  @PutMapping("/{id}")
  public StockResponse update(@PathVariable Long id, @Valid @RequestBody StockUpdateRequest request) {
    return stockService.update(id, request);
  }

  /** Eliminación lógica */
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteLogical(@PathVariable Long id) {
    stockService.deleteLogical(id);
  }

  /** Reactivar */
  @PatchMapping("/activate/{id}")
  public StockResponse activate(@PathVariable Long id) {
    return stockService.activate(id);
  }

  /** Set stock (cantidad exacta) */
  @PatchMapping("/set-quantity/{id}")
  public StockResponse setQuantity(@PathVariable Long id, @Valid @RequestBody StockSetQuantityRequest request) {
    return stockService.setQuantity(id, request);
  }

  /** Ajuste de stock (+/-) */
  @PatchMapping("/adjust/{id}")
  public StockResponse adjust(@PathVariable Long id, @Valid @RequestBody StockAdjustQuantityRequest request) {
    return stockService.adjustQuantity(id, request);
  }

  //Sumatoria total (todos los almacenes activos)
  @GetMapping("/availability")
  public StockAvailabilityResponse getAvailability(
    @RequestParam Long productId,
    @RequestParam Long measureId,
    @RequestParam Long colorId
  ) {
    return stockService.getAvailability(productId, measureId, colorId);
  }

  //Por almacén
  @GetMapping("/availability/by-warehouse")
  public StockAvailabilityResponse getAvailabilityByWarehouse(
    @RequestParam Long productId,
    @RequestParam Long measureId,
    @RequestParam Long colorId,
    @RequestParam Long warehouseId
  ) {
    return stockService.getAvailabilityByWarehouse(productId, measureId, colorId, warehouseId);
  }

}
