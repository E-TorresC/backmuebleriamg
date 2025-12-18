package com.muebleriamg.modules.products.web.controller;

import com.muebleriamg.modules.products.domain.service.ProductService;
import com.muebleriamg.modules.products.web.dto.ProductCreateRequest;
import com.muebleriamg.modules.products.web.dto.ProductResponse;
import com.muebleriamg.modules.products.web.dto.ProductUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ProductResponse create(@Valid @RequestBody ProductCreateRequest request) {
    return productService.create(request);
  }

  @GetMapping("/{id}")
  public ProductResponse getById(@PathVariable Long id) {
    return productService.getById(id);
  }

  /**
   * Lista activos. Si envía categoryId, filtra por categoría.
   * Ej: GET /api/v1/products?categoryId=1
   */
  @GetMapping
  public List<ProductResponse> getAllActive(@RequestParam(name = "categoryId", required = false) Long categoryId) {
    if (categoryId != null) {
      return productService.getAllActiveByCategory(categoryId);
    }
    return productService.getAllActive();
  }

  @PutMapping("/{id}")
  public ProductResponse update(@PathVariable Long id,
                                @Valid @RequestBody ProductUpdateRequest request) {
    return productService.update(id, request);
  }

  /** Eliminación lógica: Status => INACTIVE */
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteLogical(@PathVariable Long id) {
    productService.deleteLogical(id);
  }

  /** Reactivar: INACTIVE => ACTIVE (si no está BLOCKED) */
  @PatchMapping("/activate/{id}")
  public ProductResponse activate(@PathVariable Long id) {
    return productService.activate(id);
  }
}
