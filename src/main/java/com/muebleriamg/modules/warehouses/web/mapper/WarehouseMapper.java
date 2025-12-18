package com.muebleriamg.modules.warehouses.web.mapper;

import com.muebleriamg.modules.warehouses.domain.entity.Warehouse;
import com.muebleriamg.modules.warehouses.web.dto.WarehouseCreateRequest;
import com.muebleriamg.modules.warehouses.web.dto.WarehouseResponse;
import com.muebleriamg.modules.warehouses.web.dto.WarehouseUpdateRequest;
import org.mapstruct.*;

@Mapper(
  componentModel = "spring",
  builder = @org.mapstruct.Builder(disableBuilder = true)
)
public interface WarehouseMapper {

  WarehouseResponse toResponse(Warehouse entity);

  @Mapping(target = "warehouseId", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  Warehouse toEntity(WarehouseCreateRequest request);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(target = "warehouseId", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  void updateEntityFromRequest(WarehouseUpdateRequest request, @MappingTarget Warehouse entity);
}
