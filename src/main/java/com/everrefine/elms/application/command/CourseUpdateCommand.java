package com.everrefine.elms.application.command;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 更新用コースのコマンド。
 */
@Getter
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class CourseUpdateCommand {

  @NotNull
  private UUID id;
  @NotNull
  private String title;
  @NotNull
  private String description;
  @Nullable
  private String thumbnailUrl;
  @NotNull
  private LocalDateTime updatedAt;

  public static CourseUpdateCommand create(
      UUID id,
      String title,
      String description,
      String thumbnailUrl
  ) {
    return new CourseUpdateCommand(
        id,
        title,
        description,
        thumbnailUrl,
        LocalDateTime.now()
    );
  }
}