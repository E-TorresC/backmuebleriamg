package com.muebleriamg.modules.productcolors.web.controller;

import com.muebleriamg.modules.productcolors.domain.service.ProductColorService;
import com.muebleriamg.modules.productcolors.web.dto.ProductColorCreateRequest;
import com.muebleriamg.modules.productcolors.web.dto.ProductColorResponse;
import com.muebleriamg.modules.productcolors.web.dto.ProductColorUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product-colors")
@RequiredArgsConstructor
public class ProductColorController {

  private final ProductColorService productColorService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ProductColorResponse create(@Valid @RequestBody ProductColorCreateRequest request) {
    return productColorService.create(request);
  }

  @GetMapping("/{id}")
  public ProductColorResponse getById(@PathVariable Long id) {
    return productColorService.getById(id);
  }

  /**
   * Lista activos. Filtros opcionales:
   * - GET /api/v1/product-colors?productId=1
   * - GET /api/v1/product-colors?colorId=2
   * - GET /api/v1/product-colors?productId=1&colorId=2
   */
  @GetMapping
  public List<ProductColorResponse> getAllActive(
    @RequestParam(name = "productId", required = false) Long productId,
    @RequestParam(name = "colorId", required = false) Long colorId
  ) {
    return productColorService.getAllActiveByFilter(productId, colorId);
  }

  @PutMapping("/{id}")
  public ProductColorResponse update(@PathVariable Long id,
                                     @Valid @RequestBody ProductColorUpdateRequest request) {
    return productColorService.update(id, request);
  }

  /** Eliminación lógica: Status => INACTIVE */
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteLogical(@PathVariable Long id) {
    productColorService.deleteLogical(id);
  }

  /** Reactivar: INACTIVE => ACTIVE (si no está BLOCKED) */
  @PatchMapping("/activate/{id}")
  public ProductColorResponse activate(@PathVariable Long id) {
    return productColorService.activate(id);
  }
}
