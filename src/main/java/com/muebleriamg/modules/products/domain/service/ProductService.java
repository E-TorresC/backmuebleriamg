package com.muebleriamg.modules.products.domain.service;

import com.muebleriamg.modules.products.web.dto.ProductCreateRequest;
import com.muebleriamg.modules.products.web.dto.ProductImageVariantsResponse;
import com.muebleriamg.modules.products.web.dto.ProductResponse;
import com.muebleriamg.modules.products.web.dto.ProductUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

  ProductResponse create(ProductCreateRequest request);

  ProductResponse getById(Long id);

  List<ProductResponse> getAllActive();

  List<ProductResponse> getAllActiveByCategory(Long categoryId);

  ProductResponse update(Long id, ProductUpdateRequest request);

  void deleteLogical(Long id);

  ProductResponse activate(Long id);

  // de cloudinary
  ProductResponse uploadImage(Long productId, MultipartFile file);


  ProductResponse resetImageToDefault(Long productId, boolean removeFromCloudinary);

  ProductImageVariantsResponse getImageVariants(Long productId);
}
