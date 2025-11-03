package com.everrefine.elms.application.command;

import lombok.Data;

@Data
public class LessonUpdateCommand {

  private final Integer id;
  private final String title;
  private final String description;
  private final String videoUrl;

  public static LessonUpdateCommand create(
      Integer id,
      String title,
      String description,
      String videoUrl
  ) {
    return new LessonUpdateCommand(id, title, description, videoUrl);
  }
}
