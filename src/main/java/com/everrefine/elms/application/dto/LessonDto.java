package com.everrefine.elms.application.dto;

import com.everrefine.elms.domain.model.lesson.Lesson;
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

  public static LessonDto from(Lesson lesson) {
    return new LessonDto(
        lesson.getId(),
        lesson.getLessonGroupId(),
        lesson.getCourseId(),
        lesson.getLessonOrder().getValue(),
        lesson.getTitle().getValue(),
        lesson.getDescription() != null ? lesson.getDescription().getValue() : null,
        lesson.getVideoUrl() != null ? lesson.getVideoUrl().getValue() : null,
        lesson.getCreatedAt(),
        lesson.getUpdatedAt()
    );
  }
}
