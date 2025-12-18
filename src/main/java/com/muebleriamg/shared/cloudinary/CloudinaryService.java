package com.muebleriamg.shared.cloudinary;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
  String uploadImage(MultipartFile file, String folder, String publicId);

  void deleteImage(String publicId);

  String generateTransformedUrl(String publicId, int width, int height, String crop);
}
