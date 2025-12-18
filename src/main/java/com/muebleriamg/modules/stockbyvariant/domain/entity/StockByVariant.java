package com.muebleriamg.modules.stockbyvariant.domain.entity;

import com.muebleriamg.modules.colors.domain.entity.Color;
import com.muebleriamg.modules.measures.domain.entity.Measure;
import com.muebleriamg.modules.products.domain.entity.Product;
import com.muebleriamg.modules.warehouses.domain.entity.Warehouse;
import com.muebleriamg.shared.entities.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
  name = "StockByVariant",
  uniqueConstraints = {
    @UniqueConstraint(name = "UQ_Stock", columnNames = {"ProductID","MeasureID","ColorID","WarehouseID"})
  },
  indexes = {
    @Index(name = "IX_StockByVariant_Warehouse", columnList = "WarehouseID,ProductID,MeasureID,ColorID")
  }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockByVariant extends AuditableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "StockID")
  private Long stockId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "ProductID", nullable = false)
  private Product product;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "MeasureID", nullable = false)
  private Measure measure;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "ColorID", nullable = false)
  private Color color;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "WarehouseID", nullable = false)
  private Warehouse warehouse;

  @Column(name = "Quantity", nullable = false)
  private Integer quantity;
}
