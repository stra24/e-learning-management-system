package com.everrefine.elms.presentation.request;

import lombok.Data;

@Data
public class CourseCreateRequest {

  private String title;
  private String description;
  private String thumbnailUrl;
}