package com.muebleriamg.shared.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.muebleriamg.shared.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.Transformation;


import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {

  private final Cloudinary cloudinary;

  private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
    "image/jpeg", "image/png", "image/webp"
  );

  private static final long MAX_BYTES = 5L * 1024 * 1024; // 5MB

  @Override
  public String uploadImage(MultipartFile file, String folder, String publicId) {

    if (file == null || file.isEmpty()) {
      throw new BusinessException("Debe enviar una imagen.");
    }

    if (file.getSize() > MAX_BYTES) {
      throw new BusinessException("La imagen excede el tamaño máximo permitido (5MB).");
    }

    String contentType = file.getContentType();
    if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
      throw new BusinessException("Formato de imagen no permitido. Use JPG, PNG o WEBP.");
    }

    try {
      Map<?, ?> result = cloudinary.uploader().upload(
        file.getBytes(),
        ObjectUtils.asMap(
          "folder", folder,
          "public_id", publicId,
          "resource_type", "image",
          "overwrite", true
        )
      );

      Object secureUrl = result.get("secure_url");
      if (secureUrl == null) {
        throw new BusinessException("No se pudo obtener la URL de la imagen.");
      }

      return secureUrl.toString();

    } catch (Exception ex) {
      throw new BusinessException("Error al subir la imagen: " + ex.getMessage());
    }
  }

  @Override
  public void deleteImage(String publicId) {
    try {
      cloudinary.uploader().destroy(publicId, ObjectUtils.asMap(
        "resource_type", "image",
        "invalidate", true
      ));
    } catch (Exception ex) {
      throw new BusinessException("Error al eliminar la imagen en Cloudinary: " + ex.getMessage());
    }
  }

  @Override
  public String generateTransformedUrl(String publicId, int width, int height, String crop) {
    try {
      return cloudinary.url()
        .secure(true)
        .transformation(new Transformation<>()
          .width(width)
          .height(height)
          .crop(crop)
          .quality("auto")
          .fetchFormat("auto")
        )
        .generate(publicId);
    } catch (Exception ex) {
      throw new BusinessException("Error al generar URL transformada: " + ex.getMessage());
    }
  }

}
