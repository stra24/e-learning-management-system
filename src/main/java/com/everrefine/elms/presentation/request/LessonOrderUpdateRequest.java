package com.everrefine.elms.presentation.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LessonOrderUpdateRequest {

  private Integer precedingLessonId;

  private Integer followingLessonId;

  public LessonOrderUpdateRequest() {
  }
}
