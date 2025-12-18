package com.muebleriamg.modules.categories.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryUpdateRequest {

  @NotBlank(message = "El nombre es obligatorio.")
  @Size(max = 100, message = "El nombre no debe exceder 100 caracteres.")
  private String categoryName;

  @NotBlank(message = "El slug es obligatorio.")
  @Size(max = 120, message = "El slug no debe exceder 120 caracteres.")
  private String categorySlug;

  @Size(max = 300, message = "La descripción no debe exceder 300 caracteres.")
  private String categoryDescription;
}

