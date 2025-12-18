package com.muebleriamg.modules.productmeasures.domain.entity;

import com.muebleriamg.modules.measures.domain.entity.Measure;
import com.muebleriamg.modules.products.domain.entity.Product;
import com.muebleriamg.shared.entities.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
  name = "ProductMeasures",
  uniqueConstraints = {
    @UniqueConstraint(name = "UQ_ProductMeasure", columnNames = {"ProductID", "MeasureID"})
  }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductMeasure extends AuditableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ProductMeasureID")
  private Long productMeasureId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "ProductID", nullable = false)
  private Product product;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "MeasureID", nullable = false)
  private Measure measure;

  @Column(name = "Price", nullable = false, precision = 12, scale = 2)
  private BigDecimal price;
}
