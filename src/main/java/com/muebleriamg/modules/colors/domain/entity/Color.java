package com.muebleriamg.modules.colors.domain.entity;

import com.muebleriamg.shared.entities.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Colors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Color extends AuditableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ColorID")
  private Long colorId;

  @Column(name = "ColorName", nullable = false, length = 80)
  private String colorName;

  @Column(name = "Finish", length = 60)
  private String finish;
}

