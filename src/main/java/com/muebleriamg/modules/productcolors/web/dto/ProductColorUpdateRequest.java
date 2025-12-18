package com.muebleriamg.modules.productcolors.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductColorUpdateRequest {

  @NotNull(message = "El productId es obligatorio.")
  private Long productId;

  @NotNull(message = "El colorId es obligatorio.")
  private Long colorId;
}
