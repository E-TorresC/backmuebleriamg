package com.muebleriamg.modules.colors.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ColorCreateRequest {

  @NotBlank(message = "El nombre del color es obligatorio.")
  @Size(max = 80, message = "El nombre del color no debe exceder 80 caracteres.")
  private String colorName;

  @Size(max = 60, message = "El acabado no debe exceder 60 caracteres.")
  private String finish;
}

