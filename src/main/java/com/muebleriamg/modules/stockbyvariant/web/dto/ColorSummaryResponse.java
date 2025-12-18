package com.muebleriamg.modules.stockbyvariant.web.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ColorSummaryResponse {
  private Long colorId;
  private String colorName;
  private String finish;
}
