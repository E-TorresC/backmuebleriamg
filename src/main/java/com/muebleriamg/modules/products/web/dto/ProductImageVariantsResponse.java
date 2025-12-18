package com.muebleriamg.modules.products.web.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImageVariantsResponse {

  private Long productId;

  /** URL original guardada en BD (o default) */
  private String original;

  /** Miniaturas recomendadas para móvil/catálogo */
  private String thumbnail; // ej. 200x200
  private String small;     // ej. 400x400
  private String medium;    // ej. 800x800
  private String large;     // ej. 1200x1200
}

