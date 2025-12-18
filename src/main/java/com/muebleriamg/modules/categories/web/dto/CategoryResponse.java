package com.muebleriamg.modules.categories.web.dto;

import com.muebleriamg.shared.enums.Status;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {

  private Long categoryId;
  private String categoryName;
  private String categorySlug;
  private String categoryDescription;

  private Status status;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}

