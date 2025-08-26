package com.everrefine.elms.presentation.request;

import lombok.Data;

@Data
public class CourseUpdateRequest {

  private String courseId;
  private String courseOrder;
  private String title;
  private String description;
  private String thumbnailUrl;
}