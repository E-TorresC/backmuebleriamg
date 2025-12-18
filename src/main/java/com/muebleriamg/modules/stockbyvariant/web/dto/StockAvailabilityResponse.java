package com.muebleriamg.modules.stockbyvariant.web.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockAvailabilityResponse {

  private Long productId;
  private Long measureId;
  private Long colorId;
  private Long warehouseId; // null si es sumatoria global

  private long available;
}
