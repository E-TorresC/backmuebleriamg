package com.muebleriamg.shared.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Configuration
@EnableConfigurationProperties(CloudinaryProperties.class)
public class CloudinaryConfig {

  @Bean
  public Cloudinary cloudinary(CloudinaryProperties props) {
    return new Cloudinary(ObjectUtils.asMap(
      "cloud_name", props.getCloudName(),
      "api_key", props.getApiKey(),
      "api_secret", props.getApiSecret()
    ));
  }
}
