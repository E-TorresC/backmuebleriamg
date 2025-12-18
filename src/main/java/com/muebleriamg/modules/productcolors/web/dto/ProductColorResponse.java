package com.muebleriamg.modules.productcolors.web.dto;

import com.muebleriamg.shared.enums.Status;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductColorResponse {

  private Long productColorId;

  private ProductSummaryResponse product;
  private ColorSummaryResponse color;

  private Status status;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
