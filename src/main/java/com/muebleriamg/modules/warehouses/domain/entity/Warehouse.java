package com.muebleriamg.modules.warehouses.domain.entity;

import com.muebleriamg.shared.entities.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
  name = "Warehouses",
  uniqueConstraints = {
    @UniqueConstraint(name = "UQ_Warehouses_Name", columnNames = {"WarehouseName"}),
    @UniqueConstraint(name = "UQ_Warehouses_Slug", columnNames = {"WarehouseSlug"})
  }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Warehouse extends AuditableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "WarehouseID")
  private Long warehouseId;

  @Column(name = "WarehouseName", nullable = false, length = 120)
  private String warehouseName;

  @Column(name = "WarehouseSlug", nullable = false, length = 150)
  private String warehouseSlug;

  @Column(name = "Address", length = 200)
  private String address;
}
