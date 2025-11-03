package com.everrefine.elms.presentation.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LessonUpdateRequest {
  
  @NotBlank(message = "レッスンタイトルは必須です")
  private String title;
  
  private String description;
  
  private String videoUrl;
}
