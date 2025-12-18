package com.muebleriamg.modules.products.web.controller;

import com.muebleriamg.modules.products.domain.service.ProductService;
import com.muebleriamg.modules.products.web.dto.ProductCreateRequest;
import com.muebleriamg.modules.products.web.dto.ProductResponse;
import com.muebleriamg.modules.products.web.dto.ProductUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.muebleriamg.modules.products.web.dto.ProductImageVariantsResponse;

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

  // para cloudinary

  @PostMapping(value = "/image/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ProductResponse uploadImage(@PathVariable Long id, @RequestPart("file") MultipartFile file) {
    return productService.uploadImage(id, file);
  }

  /**
   * “Eliminar” imagen del producto (en realidad: resetear a default).
   * Si removeFromCloudinary=true, además elimina el asset en Cloudinary.
   */
  @DeleteMapping("/image/{id}")
  public ProductResponse resetImage(@PathVariable Long id,
                                    @RequestParam(name = "removeFromCloudinary", defaultValue = "false") boolean removeFromCloudinary) {
    return productService.resetImageToDefault(id, removeFromCloudinary);
  }

  /** Variantes para móvil/catálogo
   * Variantes para frontend
   * GET /api/v1/products/image/variants/{id}
   * El frontend usa:
   * thumbnail para catálogo
   * small/medium para modal / detalle
   * large si necesita zoom o vista completa
   * */
  @GetMapping("/image/variants/{id}")
  public ProductImageVariantsResponse getImageVariants(@PathVariable Long id) {
    return productService.getImageVariants(id);
  }

}
