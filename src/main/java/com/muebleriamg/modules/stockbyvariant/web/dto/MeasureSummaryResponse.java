package com.muebleriamg.modules.stockbyvariant.web.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeasureSummaryResponse {
  private Long measureId;
  private BigDecimal widthCm;
  private BigDecimal heightCm;
  private BigDecimal depthCm;
  private String capacityNote;
}
