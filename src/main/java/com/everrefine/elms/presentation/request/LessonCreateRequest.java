package com.everrefine.elms.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LessonCreateRequest {
  
  @NotBlank(message = "レッスンタイトルは必須です")
  private String title;
  
  private String description;
  
  private String videoUrl;
}
