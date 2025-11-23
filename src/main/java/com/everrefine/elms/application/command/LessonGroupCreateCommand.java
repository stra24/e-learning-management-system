package com.everrefine.elms.application.command;

import lombok.Data;

@Data
public class LessonGroupCreateCommand {

  private final Integer courseId;
  private final String title;

  public static LessonGroupCreateCommand create(
      Integer courseId,
      String title
  ) {
    return new LessonGroupCreateCommand(courseId, title);
  }
}
