package com.muebleriamg.modules.categories.web.mapper;

import com.muebleriamg.modules.categories.domain.entity.Category;
import com.muebleriamg.modules.categories.web.dto.CategoryCreateRequest;
import com.muebleriamg.modules.categories.web.dto.CategoryResponse;
import com.muebleriamg.modules.categories.web.dto.CategoryUpdateRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
  builder = @org.mapstruct.Builder(disableBuilder = true))

public interface CategoryMapper {

  CategoryResponse toResponse(Category entity);

  @Mapping(target = "categoryId", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  Category toEntity(CategoryCreateRequest request);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(target = "categoryId", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  void updateEntityFromRequest(CategoryUpdateRequest request, @MappingTarget Category entity);
}
