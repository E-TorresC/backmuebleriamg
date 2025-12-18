package com.muebleriamg.modules.warehouses.web.dto;

import com.muebleriamg.shared.enums.Status;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarehouseResponse {

  private Long warehouseId;
  private String warehouseName;
  private String warehouseSlug;
  private String address;

  private Status status;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
