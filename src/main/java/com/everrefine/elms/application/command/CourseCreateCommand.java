package com.everrefine.elms.application.command;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 新規作成用コースのコマンド。
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CourseCreateCommand {

  @NotNull
  private UUID id;
  @NotNull
  private String title;
  @NotNull
  private String description;
  @Nullable
  private String thumbnailUrl;

  public static CourseCreateCommand create(
      String title,
      String description,
      String thumbnailUrl
  ) {
    return new CourseCreateCommand(
        UUID.randomUUID(),
        title,
        description,
        thumbnailUrl
    );
  }
}