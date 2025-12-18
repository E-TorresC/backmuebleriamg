package com.muebleriamg.modules.measures.web.mapper;

import com.muebleriamg.modules.measures.domain.entity.Measure;
import com.muebleriamg.modules.measures.web.dto.MeasureCreateRequest;
import com.muebleriamg.modules.measures.web.dto.MeasureResponse;
import com.muebleriamg.modules.measures.web.dto.MeasureUpdateRequest;
import org.mapstruct.*;

@Mapper(
  componentModel = "spring",
  builder = @org.mapstruct.Builder(disableBuilder = true)
)
public interface MeasureMapper {

  MeasureResponse toResponse(Measure entity);

  @Mapping(target = "measureId", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  Measure toEntity(MeasureCreateRequest request);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(target = "measureId", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  void updateEntityFromRequest(MeasureUpdateRequest request, @MappingTarget Measure entity);
}

