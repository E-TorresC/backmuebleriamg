package com.muebleriamg.modules.productmeasures.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductMeasureUpdateRequest {

  @NotNull(message = "El productId es obligatorio.")
  private Long productId;

  @NotNull(message = "El measureId es obligatorio.")
  private Long measureId;

  @NotNull(message = "El precio es obligatorio.")
  @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0.")
  @Digits(integer = 10, fraction = 2, message = "El precio debe tener como máximo 10 enteros y 2 decimales.")
  private BigDecimal price;
}
