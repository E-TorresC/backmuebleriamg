package com.muebleriamg.modules.categories.web.controller;

import com.muebleriamg.modules.categories.domain.service.CategoryService;
import com.muebleriamg.modules.categories.web.dto.CategoryCreateRequest;
import com.muebleriamg.modules.categories.web.dto.CategoryResponse;
import com.muebleriamg.modules.categories.web.dto.CategoryUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

  private final CategoryService categoryService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CategoryResponse create(@Valid @RequestBody CategoryCreateRequest request) {
    return categoryService.create(request);
  }

  @GetMapping("/{id}")
  public CategoryResponse getById(@PathVariable Long id) {
    return categoryService.getById(id);
  }

  @GetMapping
  public List<CategoryResponse> getAllActive() {
    return categoryService.getAllActive();
  }

  @PutMapping("/{id}")
  public CategoryResponse update(@PathVariable Long id,
                                 @Valid @RequestBody CategoryUpdateRequest request) {
    return categoryService.update(id, request);
  }

  /** Eliminación lógica: Status => INACTIVE */
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteLogical(@PathVariable Long id) {
    categoryService.deleteLogical(id);
  }

  @PatchMapping("/activate/{id}")
  public CategoryResponse activate(@PathVariable Long id) {
    return categoryService.activate(id);
  }

}

