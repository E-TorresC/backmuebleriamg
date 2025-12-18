package com.muebleriamg.shared.entities;

import com.muebleriamg.shared.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class AuditableEntity {

  @Enumerated(EnumType.STRING)
  @Column(name = "Status", nullable = false)
  private Status status = Status.ACTIVE;

  @CreationTimestamp
  @Column(name = "CreatedAt", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "UpdatedAt", nullable = false)
  private LocalDateTime updatedAt;
}
