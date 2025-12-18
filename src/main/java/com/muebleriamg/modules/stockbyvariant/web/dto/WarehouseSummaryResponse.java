package com.muebleriamg.modules.stockbyvariant.web.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarehouseSummaryResponse {
  private Long warehouseId;
  private String warehouseName;
  private String warehouseSlug;
}
