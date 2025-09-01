package com.everrefine.elms.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LessonDto {
  private final UUID id;
  private final UUID lessonGroupId;
  private final UUID courseId;
  private final BigDecimal lessonOrder;
  private final String title;
  private final String description;
  private final String videoUrl;
  private final LocalDateTime createdAt;
  private final LocalDateTime updatedAt;
}
