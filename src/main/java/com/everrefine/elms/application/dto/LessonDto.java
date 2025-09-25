package com.everrefine.elms.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LessonDto {
  private final Integer id;
  private final Integer lessonGroupId;
  private final Integer courseId;
  private final BigDecimal lessonOrder;
  private final String title;
  private final String description;
  private final String videoUrl;
  private final LocalDateTime createdAt;
  private final LocalDateTime updatedAt;
}
