package com.muebleriamg.modules.productmeasures.web.dto;

import com.muebleriamg.shared.enums.Status;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductMeasureResponse {

  private Long productMeasureId;
  private BigDecimal price;

  private ProductSummaryResponse product;
  private MeasureSummaryResponse measure;

  private Status status;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
