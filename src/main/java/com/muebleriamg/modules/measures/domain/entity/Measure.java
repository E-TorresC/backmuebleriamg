package com.muebleriamg.modules.measures.domain.entity;

import com.muebleriamg.shared.entities.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
  name = "Measures",
  uniqueConstraints = {
    @UniqueConstraint(name = "UQ_Measures", columnNames = {"WidthCm", "HeightCm", "DepthCm"})
  }
)
public class Measure extends AuditableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "MeasureID")
  private Long measureId;

  @Column(name = "WidthCm", nullable = false, precision = 8, scale = 2)
  private BigDecimal widthCm;

  @Column(name = "HeightCm", nullable = false, precision = 8, scale = 2)
  private BigDecimal heightCm;

  @Column(name = "DepthCm", nullable = false, precision = 8, scale = 2)
  private BigDecimal depthCm;

  @Column(name = "CapacityNote", length = 120)
  private String capacityNote;
}

