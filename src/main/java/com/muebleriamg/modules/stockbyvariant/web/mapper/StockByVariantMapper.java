package com.muebleriamg.modules.stockbyvariant.web.mapper;

import com.muebleriamg.modules.stockbyvariant.domain.entity.StockByVariant;
import com.muebleriamg.modules.stockbyvariant.web.dto.StockCreateRequest;
import com.muebleriamg.modules.stockbyvariant.web.dto.StockResponse;
import com.muebleriamg.modules.stockbyvariant.web.dto.StockUpdateRequest;
import org.mapstruct.*;

@Mapper(
  componentModel = "spring",
  builder = @org.mapstruct.Builder(disableBuilder = true)
)
public interface StockByVariantMapper {

  @Mappings({
    @Mapping(source = "product.productId", target = "product.productId"),
    @Mapping(source = "product.productName", target = "product.productName"),

    @Mapping(source = "measure.measureId", target = "measure.measureId"),
    @Mapping(source = "measure.widthCm", target = "measure.widthCm"),
    @Mapping(source = "measure.heightCm", target = "measure.heightCm"),
    @Mapping(source = "measure.depthCm", target = "measure.depthCm"),
    @Mapping(source = "measure.capacityNote", target = "measure.capacityNote"),

    @Mapping(source = "color.colorId", target = "color.colorId"),
    @Mapping(source = "color.colorName", target = "color.colorName"),
    @Mapping(source = "color.finish", target = "color.finish"),

    @Mapping(source = "warehouse.warehouseId", target = "warehouse.warehouseId"),
    @Mapping(source = "warehouse.warehouseName", target = "warehouse.warehouseName"),
    @Mapping(source = "warehouse.warehouseSlug", target = "warehouse.warehouseSlug")
  })
  StockResponse toResponse(StockByVariant entity);

  @Mapping(target = "stockId", ignore = true)
  @Mapping(target = "product", ignore = true)
  @Mapping(target = "measure", ignore = true)
  @Mapping(target = "color", ignore = true)
  @Mapping(target = "warehouse", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  StockByVariant toEntity(StockCreateRequest request);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(target = "stockId", ignore = true)
  @Mapping(target = "product", ignore = true)
  @Mapping(target = "measure", ignore = true)
  @Mapping(target = "color", ignore = true)
  @Mapping(target = "warehouse", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  void updateEntityFromRequest(StockUpdateRequest request, @MappingTarget StockByVariant entity);
}
