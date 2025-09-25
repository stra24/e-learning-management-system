package com.everrefine.elms.application.command;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class LessonCreateCommand {
  
  private final Integer courseId;
  private final Integer lessonGroupId;
  private final String title;
  private final String description;
  private final String videoUrl;
  private final BigDecimal lessonOrder;
  
  public static LessonCreateCommand create(
      Integer courseId,
      Integer lessonGroupId,
      String title,
      String description,
      String videoUrl
  ) {
    return new LessonCreateCommand(courseId, lessonGroupId, title, description, videoUrl, null);
  }
  
  public LessonCreateCommand updateLessonOrder(
      BigDecimal lessonOrder
  ) {
    return new LessonCreateCommand(courseId, lessonGroupId, title, description, videoUrl, lessonOrder);
  }
}
