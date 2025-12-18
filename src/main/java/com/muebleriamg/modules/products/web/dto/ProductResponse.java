package com.muebleriamg.modules.products.web.dto;

import com.muebleriamg.shared.enums.Status;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {

  private Long productId;
  private String productName;
  private String productDescription;
  private String productImageUrl;

  private CategorySummaryResponse category;

  private Status status;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
