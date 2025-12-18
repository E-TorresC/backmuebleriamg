package com.muebleriamg.modules.stockbyvariant.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockAdjustQuantityRequest {

  /** Puede ser positivo o negativo. */
  @NotNull(message = "El delta es obligatorio.")
  private Integer delta;
}
