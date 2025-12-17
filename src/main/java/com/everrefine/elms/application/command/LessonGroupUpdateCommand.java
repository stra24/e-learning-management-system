package com.everrefine.elms.application.command;

import lombok.Data;

@Data
public class LessonGroupUpdateCommand {

  private final Integer id;
  private final String title;

  public static LessonGroupUpdateCommand create(
      Integer id,
      String title
  ) {
    return new LessonGroupUpdateCommand(id, title);
  }
}
