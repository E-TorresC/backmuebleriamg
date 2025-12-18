package com.muebleriamg.modules.productmeasures.web.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSummaryResponse {
  private Long productId;
  private String productName;
}
