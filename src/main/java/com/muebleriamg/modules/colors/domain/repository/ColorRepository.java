package com.muebleriamg.modules.colors.domain.repository;

import com.muebleriamg.modules.colors.domain.entity.Color;
import com.muebleriamg.shared.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ColorRepository extends JpaRepository<Color, Long> {

  List<Color> findAllByStatus(Status status);

  Optional<Color> findByColorIdAndStatus(Long colorId, Status status);
}
