package com.everrefine.elms.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LessonGroupUpdateRequest {
  
  @NotBlank(message = "レッスングループタイトルは必須です")
  @Size(max = 100, message = "タイトルは100文字以内で入力してください")
  private String title;
}
