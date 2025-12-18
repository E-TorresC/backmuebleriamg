package com.muebleriamg.modules.products.web.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategorySummaryResponse {
  private Long categoryId;
  private String categoryName;
  private String categorySlug;
}
