package com.everrefine.elms.application.command;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NewsCreateCommand {

  private Integer id;
  @NotNull
  private String title;
  @NotNull
  private String content;

  public static NewsCreateCommand create(
      String title,
      String content
  ) {
    return new NewsCreateCommand(
        null,
        title,
        content
    );
  }
}
