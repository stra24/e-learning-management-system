package com.everrefine.elms.application.command;

import jakarta.annotation.Nullable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "create")
public class LessonSearchCommand {

  private int pageNum;
  private int pageSize;
  @Nullable
  private String courseId;
  @Nullable
  private String lessonGroupId;
  @Nullable
  private String title;
  @Nullable
  private LocalDate createdDateFrom;
  @Nullable
  private LocalDate createdDateTo;
}
