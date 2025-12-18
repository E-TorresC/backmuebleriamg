package com.muebleriamg.modules.warehouses.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarehouseUpdateRequest {

  @NotBlank(message = "El nombre del almacén es obligatorio.")
  @Size(max = 120, message = "El nombre no debe exceder 120 caracteres.")
  private String warehouseName;

  @NotBlank(message = "El slug del almacén es obligatorio.")
  @Size(max = 150, message = "El slug no debe exceder 150 caracteres.")
  private String warehouseSlug;

  @Size(max = 200, message = "La dirección no debe exceder 200 caracteres.")
  private String address;
}
