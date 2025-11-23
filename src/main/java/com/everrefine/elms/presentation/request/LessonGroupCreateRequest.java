package com.everrefine.elms.presentation.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LessonGroupCreateRequest {

  @NotBlank(message = "レッスングループタイトルは必須です")
  private String title;
}
