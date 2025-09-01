package com.everrefine.elms.presentation.request;

import jakarta.annotation.Nullable;
import java.time.LocalDate;
import lombok.Data;

@Data
public class LessonSearchRequest {

  private int pageNum = 1;
  private int pageSize = 10;

  @Nullable
  private String courseId;

  @Nullable
  private String lessonGroupId;

  @Nullable
  private String title;

  @Nullable
  LocalDate createdDateFrom;

  @Nullable
  LocalDate createdDateTo;
}
