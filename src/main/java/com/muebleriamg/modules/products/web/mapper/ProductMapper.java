package com.muebleriamg.modules.products.web.mapper;

import com.muebleriamg.modules.products.domain.entity.Product;
import com.muebleriamg.modules.products.web.dto.ProductCreateRequest;
import com.muebleriamg.modules.products.web.dto.ProductResponse;
import com.muebleriamg.modules.products.web.dto.ProductUpdateRequest;
import org.mapstruct.*;

@Mapper(
  componentModel = "spring",
  builder = @org.mapstruct.Builder(disableBuilder = true)
)
public interface ProductMapper {

  @Mappings({
    @Mapping(source = "category.categoryId", target = "category.categoryId"),
    @Mapping(source = "category.categoryName", target = "category.categoryName"),
    @Mapping(source = "category.categorySlug", target = "category.categorySlug")
  })
  ProductResponse toResponse(Product entity);

  @Mapping(target = "productId", ignore = true)
  @Mapping(target = "category", ignore = true) // se setea en el service
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  Product toEntity(ProductCreateRequest request);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(target = "productId", ignore = true)
  @Mapping(target = "category", ignore = true) // se setea en el service
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  void updateEntityFromRequest(ProductUpdateRequest request, @MappingTarget Product entity);
}
