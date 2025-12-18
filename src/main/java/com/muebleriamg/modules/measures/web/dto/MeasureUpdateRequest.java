package com.muebleriamg.modules.measures.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeasureUpdateRequest {

  @NotNull(message = "El ancho es obligatorio.")
  @DecimalMin(value = "0.01", message = "El ancho debe ser mayor a 0.")
  @Digits(integer = 6, fraction = 2, message = "El ancho debe tener como máximo 6 enteros y 2 decimales.")
  private BigDecimal widthCm;

  @NotNull(message = "El alto es obligatorio.")
  @DecimalMin(value = "0.01", message = "El alto debe ser mayor a 0.")
  @Digits(integer = 6, fraction = 2, message = "El alto debe tener como máximo 6 enteros y 2 decimales.")
  private BigDecimal heightCm;

  @NotNull(message = "La profundidad es obligatoria.")
  @DecimalMin(value = "0.01", message = "La profundidad debe ser mayor a 0.")
  @Digits(integer = 6, fraction = 2, message = "La profundidad debe tener como máximo 6 enteros y 2 decimales.")
  private BigDecimal depthCm;

  @Size(max = 120, message = "La nota de capacidad no debe exceder 120 caracteres.")
  private String capacityNote;
}

