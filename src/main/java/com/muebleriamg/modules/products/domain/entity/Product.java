package com.muebleriamg.modules.products.domain.entity;

import com.muebleriamg.modules.categories.domain.entity.Category;
import com.muebleriamg.shared.entities.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
  name = "Products",
  uniqueConstraints = {
    @UniqueConstraint(name = "UQ_Products", columnNames = {"CategoryID", "ProductName"})
  }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product extends AuditableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ProductID")
  private Long productId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "CategoryID", nullable = false)
  private Category category;

  @Column(name = "ProductName", nullable = false, length = 150)
  private String productName;

  @Column(name = "ProductDescription", length = 500)
  private String productDescription;

  @Column(name = "ProductImageUrl", length = 300)
  private String productImageUrl;

}
