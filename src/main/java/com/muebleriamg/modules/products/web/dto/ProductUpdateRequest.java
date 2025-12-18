package com.muebleriamg.modules.products.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductUpdateRequest {

  @NotNull(message = "El categoryId es obligatorio.")
  private Long categoryId;

  @NotBlank(message = "El nombre del producto es obligatorio.")
  @Size(max = 150, message = "El nombre no debe exceder 150 caracteres.")
  private String productName;

  @Size(max = 500, message = "La descripción no debe exceder 500 caracteres.")
  private String productDescription;

  @Size(max = 300, message = "La imagen no debe exceder 300 caracteres.")
  private String productImageUrl;

}
