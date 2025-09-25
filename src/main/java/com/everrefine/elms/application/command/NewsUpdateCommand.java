package com.everrefine.elms.application.command;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NewsUpdateCommand {

  @NotNull
  private Integer id;
  @NotNull
  private String title;
  @NotNull
  private String content;
  @NotNull
  private LocalDateTime updatedAt;

  public static NewsUpdateCommand create(
      Integer id,
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
