package com.muebleriamg.modules.colors.web.dto;

import com.muebleriamg.shared.enums.Status;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ColorResponse {

  private Long colorId;
  private String colorName;
  private String finish;

  private Status status;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
