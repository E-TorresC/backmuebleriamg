package com.muebleriamg.modules.productmeasures.web.controller;

import com.muebleriamg.modules.productmeasures.domain.service.ProductMeasureService;
import com.muebleriamg.modules.productmeasures.web.dto.ProductMeasureCreateRequest;
import com.muebleriamg.modules.productmeasures.web.dto.ProductMeasureResponse;
import com.muebleriamg.modules.productmeasures.web.dto.ProductMeasureUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product-measures")
@RequiredArgsConstructor
public class ProductMeasureController {

  private final ProductMeasureService productMeasureService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ProductMeasureResponse create(@Valid @RequestBody ProductMeasureCreateRequest request) {
    return productMeasureService.create(request);
  }

  @GetMapping("/{id}")
  public ProductMeasureResponse getById(@PathVariable Long id) {
    return productMeasureService.getById(id);
  }

  /**
   * Lista activos. Filtros opcionales:
   * - GET /api/v1/product-measures?productId=1
   * - GET /api/v1/product-measures?measureId=2
   * - GET /api/v1/product-measures?productId=1&measureId=2
   */
  @GetMapping
  public List<ProductMeasureResponse> getAllActive(
    @RequestParam(name = "productId", required = false) Long productId,
    @RequestParam(name = "measureId", required = false) Long measureId
  ) {
    return productMeasureService.getAllActiveByFilter(productId, measureId);
  }

  @PutMapping("/{id}")
  public ProductMeasureResponse update(@PathVariable Long id,
                                       @Valid @RequestBody ProductMeasureUpdateRequest request) {
    return productMeasureService.update(id, request);
  }

  /** Eliminación lógica: Status => INACTIVE */
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteLogical(@PathVariable Long id) {
    productMeasureService.deleteLogical(id);
  }

  /** Reactivar: INACTIVE => ACTIVE (si no está BLOCKED) */
  @PatchMapping("/activate/{id}")
  public ProductMeasureResponse activate(@PathVariable Long id) {
    return productMeasureService.activate(id);
  }
}
