package com.muebleriamg.modules.stockbyvariant.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockCreateRequest {

  @NotNull(message = "El productId es obligatorio.")
  private Long productId;

  @NotNull(message = "El measureId es obligatorio.")
  private Long measureId;

  @NotNull(message = "El colorId es obligatorio.")
  private Long colorId;

  @NotNull(message = "El warehouseId es obligatorio.")
  private Long warehouseId;

  @NotNull(message = "La cantidad es obligatoria.")
  @Min(value = 0, message = "La cantidad no puede ser negativa.")
  private Integer quantity;
}
