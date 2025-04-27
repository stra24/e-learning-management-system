package com.everrefine.elms;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/api/**")
        .allowedOrigins("http://localhost:3000") // Next.js開発環境
        .allowedMethods("*")
        .allowCredentials(true);
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    // プロジェクト直下のuploadsフォルダを/static/uploadsとして公開する設定
    Path uploadDir = Paths.get("uploads");
    String uploadPath = uploadDir.toFile().getAbsolutePath();

    registry.addResourceHandler("/uploads/**")
        .addResourceLocations("file:" + uploadPath + "/");
  }
}
