package com.muebleriamg.modules.stockbyvariant.web.dto;

import com.muebleriamg.shared.enums.Status;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockResponse {

  private Long stockId;

  private ProductSummaryResponse product;
  private MeasureSummaryResponse measure;
  private ColorSummaryResponse color;
  private WarehouseSummaryResponse warehouse;

  private Integer quantity;

  private Status status;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
