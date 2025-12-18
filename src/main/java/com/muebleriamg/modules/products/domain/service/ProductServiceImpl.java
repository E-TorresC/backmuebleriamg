package com.muebleriamg.modules.products.domain.service;

import com.muebleriamg.modules.categories.domain.entity.Category;
import com.muebleriamg.modules.categories.domain.repository.CategoryRepository;
import com.muebleriamg.modules.products.domain.entity.Product;
import com.muebleriamg.modules.products.domain.repository.ProductRepository;
import com.muebleriamg.modules.products.web.dto.ProductCreateRequest;
import com.muebleriamg.modules.products.web.dto.ProductImageVariantsResponse;
import com.muebleriamg.modules.products.web.dto.ProductResponse;
import com.muebleriamg.modules.products.web.dto.ProductUpdateRequest;
import com.muebleriamg.modules.products.web.mapper.ProductMapper;
import com.muebleriamg.shared.cloudinary.CloudinaryService;
import com.muebleriamg.shared.enums.Status;
import com.muebleriamg.shared.exceptions.BusinessException;
import com.muebleriamg.shared.exceptions.ResourceConflictException;
import com.muebleriamg.shared.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;
  private final ProductMapper productMapper;

  private final CloudinaryService cloudinaryService;

  @Value("${product.image.default-url}")
  private String defaultImageUrl;

  private static final String PRODUCT_IMAGE_FOLDER = "muebleriamg/products";


  @Override
  @Transactional
  public ProductResponse create(ProductCreateRequest request) {

    Category category = categoryRepository.findByCategoryIdAndStatus(request.getCategoryId(), Status.ACTIVE)
      .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada o inactiva."));

    if (productRepository.existsByCategory_CategoryIdAndProductNameIgnoreCase(
      request.getCategoryId(), request.getProductName()
    )) {
      throw new ResourceConflictException("Ya existe un producto con ese nombre en la categoría seleccionada.");
    }

    Product entity = productMapper.toEntity(request);
    entity.setCategory(category);
    entity.setStatus(Status.ACTIVE);
    entity.setProductImageUrl("https://res.cloudinary.com/do4l2xa3x/image/upload/v1766036923/logoMG_hoyl7r.png");

    Product saved = productRepository.save(entity);
    return productMapper.toResponse(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public ProductResponse getById(Long id) {

    Product entity = productRepository.findByProductIdAndStatus(id, Status.ACTIVE)
      .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado o inactivo."));

    return productMapper.toResponse(entity);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ProductResponse> getAllActive() {
    return productRepository.findAllByStatus(Status.ACTIVE)
      .stream()
      .map(productMapper::toResponse)
      .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<ProductResponse> getAllActiveByCategory(Long categoryId) {

    // Validación opcional: si desea exigir que exista la categoría
    categoryRepository.findByCategoryIdAndStatus(categoryId, Status.ACTIVE)
      .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada o inactiva."));

    return productRepository.findAllByCategory_CategoryIdAndStatus(categoryId, Status.ACTIVE)
      .stream()
      .map(productMapper::toResponse)
      .toList();
  }

  @Override
  @Transactional
  public ProductResponse update(Long id, ProductUpdateRequest request) {

    Product entity = productRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado."));

    if (entity.getStatus() != Status.ACTIVE) {
      throw new ResourceNotFoundException("No se puede actualizar: el producto no está activo.");
    }

    Category category = categoryRepository.findByCategoryIdAndStatus(request.getCategoryId(), Status.ACTIVE)
      .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada o inactiva."));

    if (productRepository.existsByCategory_CategoryIdAndProductNameIgnoreCaseAndProductIdNot(
      request.getCategoryId(), request.getProductName(), id
    )) {
      throw new ResourceConflictException("Ya existe otro producto con ese nombre en la categoría seleccionada.");
    }

    productMapper.updateEntityFromRequest(request, entity);
    entity.setCategory(category);

    Product saved = productRepository.save(entity);
    return productMapper.toResponse(saved);
  }

  @Override
  @Transactional
  public void deleteLogical(Long id) {

    Product entity = productRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado."));

    if (entity.getStatus() == Status.INACTIVE) {
      return; // idempotente
    }

    entity.setStatus(Status.INACTIVE);
    productRepository.save(entity);
  }

  @Override
  @Transactional
  public ProductResponse activate(Long id) {

    Product entity = productRepository.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado."));

    if (entity.getStatus() == Status.BLOCKED) {
      throw new BusinessException("No se puede activar: el producto está bloqueado.");
    }

    entity.setStatus(Status.ACTIVE);
    Product saved = productRepository.save(entity);

    return productMapper.toResponse(saved);
  }

  @Override
  @Transactional
  public ProductResponse uploadImage(Long productId, MultipartFile file) {

    Product product = productRepository.findById(productId)
      .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado."));

    if (product.getStatus() != Status.ACTIVE) {
      throw new ResourceNotFoundException("No se puede subir imagen: el producto no está activo.");
    }

    // Sugerencia de ruta y nombre estable (overwrite=true)
    String folder = "muebleriamg/products";
    String publicId = "product-" + productId;

    String url = cloudinaryService.uploadImage(file, folder, publicId);

    product.setProductImageUrl(url);
    Product saved = productRepository.save(product);

    return productMapper.toResponse(saved);
  }

  @Override
  @Transactional
  public ProductResponse resetImageToDefault(Long productId, boolean removeFromCloudinary) {

    Product product = productRepository.findById(productId)
      .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado."));

    if (product.getStatus() != Status.ACTIVE) {
      throw new ResourceNotFoundException("No se puede modificar imagen: el producto no está activo.");
    }

    // Si elige eliminar realmente del storage
    if (removeFromCloudinary) {
      String publicId = PRODUCT_IMAGE_FOLDER + "/product-" + productId;
      cloudinaryService.deleteImage(publicId);
    }

    product.setProductImageUrl(defaultImageUrl);
    Product saved = productRepository.save(product);

    return productMapper.toResponse(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public ProductImageVariantsResponse getImageVariants(Long productId) {

    Product product = productRepository.findById(productId)
      .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado."));

    if (product.getStatus() != Status.ACTIVE) {
      throw new ResourceNotFoundException("Producto no activo.");
    }

    String original = product.getProductImageUrl();
    if (original == null || original.isBlank()) {
      original = defaultImageUrl;
    }

    // Si la imagen no proviene de Cloudinary, se devuelve la misma URL en todas las variantes
    boolean isCloudinary = original.contains("res.cloudinary.com") && original.contains("/image/upload/");
    if (!isCloudinary) {
      return ProductImageVariantsResponse.builder()
        .productId(productId)
        .original(original)
        .thumbnail(original)
        .small(original)
        .medium(original)
        .large(original)
        .build();
    }

    // Se asume el publicId estable usado por su backend en la subida (overwrite=true)
    String publicId = PRODUCT_IMAGE_FOLDER + "/product-" + productId;

    return ProductImageVariantsResponse.builder()
      .productId(productId)
      .original(original)
      .thumbnail(cloudinaryService.generateTransformedUrl(publicId, 200, 200, "fill"))
      .small(cloudinaryService.generateTransformedUrl(publicId, 400, 400, "fill"))
      .medium(cloudinaryService.generateTransformedUrl(publicId, 800, 800, "fill"))
      .large(cloudinaryService.generateTransformedUrl(publicId, 1200, 1200, "fill"))
      .build();
  }


}
