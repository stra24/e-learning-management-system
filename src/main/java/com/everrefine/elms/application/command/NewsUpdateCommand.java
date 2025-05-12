package com.everrefine.elms.application.command;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NewsUpdateCommand {

  @NotNull
  private UUID id;
  @NotNull
  private String title;
  @NotNull
  private String content;
  @NotNull
  private LocalDateTime updatedAt;

  public static NewsUpdateCommand create(
      UUID id,
      String title,
      String content
  ) {
    return new NewsUpdateCommand(
        id,
        title,
        content,
        LocalDateTime.now()
    );
  }
}
