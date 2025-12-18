package com.muebleriamg.modules.productcolors.web.mapper;

import com.muebleriamg.modules.productcolors.domain.entity.ProductColor;
import com.muebleriamg.modules.productcolors.web.dto.ProductColorCreateRequest;
import com.muebleriamg.modules.productcolors.web.dto.ProductColorResponse;
import com.muebleriamg.modules.productcolors.web.dto.ProductColorUpdateRequest;
import org.mapstruct.*;

@Mapper(
  componentModel = "spring",
  builder = @org.mapstruct.Builder(disableBuilder = true)
)
public interface ProductColorMapper {

  @Mappings({
    @Mapping(source = "product.productId", target = "product.productId"),
    @Mapping(source = "product.productName", target = "product.productName"),

    @Mapping(source = "color.colorId", target = "color.colorId"),
    @Mapping(source = "color.colorName", target = "color.colorName"),
    @Mapping(source = "color.finish", target = "color.finish")
  })
  ProductColorResponse toResponse(ProductColor entity);

  @Mapping(target = "productColorId", ignore = true)
  @Mapping(target = "product", ignore = true) // se setea en service
  @Mapping(target = "color", ignore = true)   // se setea en service
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  ProductColor toEntity(ProductColorCreateRequest request);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(target = "productColorId", ignore = true)
  @Mapping(target = "product", ignore = true)
  @Mapping(target = "color", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  void updateEntityFromRequest(ProductColorUpdateRequest request, @MappingTarget ProductColor entity);
}
