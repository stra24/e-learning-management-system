package com.everrefine.elms.application.command;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

/**
 * 新規作成用コースのコマンド。
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CourseCreateCommand {

  @Nullable
  private Integer id;
  @Nullable
  private String thumbnailUrl;
  @NotNull
  private String title;
  @Nullable
  private String description;

  public static CourseCreateCommand create(
      String thumbnailUrl,
      String title,
      String description
  ) {
    return new CourseCreateCommand(
        null,
        thumbnailUrl,
        title,
        description
    );
  }
}