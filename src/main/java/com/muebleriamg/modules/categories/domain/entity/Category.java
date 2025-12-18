package com.muebleriamg.modules.categories.domain.entity;

import com.muebleriamg.shared.entities.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
  name = "Categories",
  uniqueConstraints = {
    @UniqueConstraint(name = "UQ_Categories_Name", columnNames = {"CategoryName"}),
    @UniqueConstraint(name = "UQ_Categories_Slug", columnNames = {"CategorySlug"})
  }
)
public class Category extends AuditableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "CategoryID")
  private Long categoryId;

  @Column(name = "CategoryName", nullable = false, length = 100)
  private String categoryName;

  @Column(name = "CategorySlug", nullable = false, length = 120)
  private String categorySlug;

  @Column(name = "CategoryDescription", length = 300)
  private String categoryDescription;
}

