package com.muebleriamg.modules.colors.web.mapper;

import com.muebleriamg.modules.colors.domain.entity.Color;
import com.muebleriamg.modules.colors.web.dto.ColorCreateRequest;
import com.muebleriamg.modules.colors.web.dto.ColorResponse;
import com.muebleriamg.modules.colors.web.dto.ColorUpdateRequest;
import org.mapstruct.*;

@Mapper(
  componentModel = "spring",
  builder = @org.mapstruct.Builder(disableBuilder = true)
)
public interface ColorMapper {

  ColorResponse toResponse(Color entity);

  @Mapping(target = "colorId", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  Color toEntity(ColorCreateRequest request);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(target = "colorId", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  void updateEntityFromRequest(ColorUpdateRequest request, @MappingTarget Color entity);
}
