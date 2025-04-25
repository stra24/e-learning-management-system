package com.everrefine.elms.application.dto;

import com.everrefine.elms.domain.model.course.Course;
import java.util.UUID;
import lombok.Getter;

@Getter
public class CourseDto {

  private final UUID id;
  private final String thumbnailUrl;
  private final String title;
  private final String description;

  // コンストラクタ
  public CourseDto(Course course) {
    this.id = course.getId();
    this.thumbnailUrl = (course.getThumbnailUrl() != null)
        ? course.getThumbnailUrl().getValue()
        : null;
    this.title = course.getTitle().getValue();
    this.description = (course.getDescription() != null)
        ? course.getDescription().getValue()
        : null;
  }
}