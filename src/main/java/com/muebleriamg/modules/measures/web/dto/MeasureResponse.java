package com.muebleriamg.modules.measures.web.dto;

import com.muebleriamg.shared.enums.Status;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeasureResponse {

  private Long measureId;
  private BigDecimal widthCm;
  private BigDecimal heightCm;
  private BigDecimal depthCm;
  private String capacityNote;

  private Status status;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}

