package com.muebleriamg.modules.productcolors.domain.entity;

import com.muebleriamg.modules.colors.domain.entity.Color;
import com.muebleriamg.modules.products.domain.entity.Product;
import com.muebleriamg.shared.entities.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ProductColors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductColor extends AuditableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ProductColorID")
  private Long productColorId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "ProductID", nullable = false)
  private Product product;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "ColorID", nullable = false)
  private Color color;
}
