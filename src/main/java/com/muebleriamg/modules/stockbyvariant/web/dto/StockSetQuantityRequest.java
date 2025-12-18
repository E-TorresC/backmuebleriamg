package com.muebleriamg.modules.stockbyvariant.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockSetQuantityRequest {

  @NotNull(message = "La cantidad es obligatoria.")
  @Min(value = 0, message = "La cantidad no puede ser negativa.")
  private Integer quantity;
}
