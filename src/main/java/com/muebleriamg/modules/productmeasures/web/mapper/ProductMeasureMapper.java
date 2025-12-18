package com.muebleriamg.modules.productmeasures.web.mapper;

import com.muebleriamg.modules.productmeasures.domain.entity.ProductMeasure;
import com.muebleriamg.modules.productmeasures.web.dto.ProductMeasureCreateRequest;
import com.muebleriamg.modules.productmeasures.web.dto.ProductMeasureResponse;
import com.muebleriamg.modules.productmeasures.web.dto.ProductMeasureUpdateRequest;
import org.mapstruct.*;

@Mapper(
  componentModel = "spring",
  builder = @org.mapstruct.Builder(disableBuilder = true)
)
public interface ProductMeasureMapper {

  @Mappings({
    @Mapping(source = "product.productId", target = "product.productId"),
    @Mapping(source = "product.productName", target = "product.productName"),

    @Mapping(source = "measure.measureId", target = "measure.measureId"),
    @Mapping(source = "measure.widthCm", target = "measure.widthCm"),
    @Mapping(source = "measure.heightCm", target = "measure.heightCm"),
    @Mapping(source = "measure.depthCm", target = "measure.depthCm"),
    @Mapping(source = "measure.capacityNote", target = "measure.capacityNote")
  })
  ProductMeasureResponse toResponse(ProductMeasure entity);

  @Mapping(target = "productMeasureId", ignore = true)
  @Mapping(target = "product", ignore = true) // se setea en service
  @Mapping(target = "measure", ignore = true) // se setea en service
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  ProductMeasure toEntity(ProductMeasureCreateRequest request);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(target = "productMeasureId", ignore = true)
  @Mapping(target = "product", ignore = true)
  @Mapping(target = "measure", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  void updateEntityFromRequest(ProductMeasureUpdateRequest request, @MappingTarget ProductMeasure entity);
}
