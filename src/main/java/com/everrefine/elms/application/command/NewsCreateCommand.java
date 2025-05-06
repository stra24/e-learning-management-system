package com.everrefine.elms.application.command;


import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NewsCreateCommand {

  @NotNull
  private UUID id;
  @NotNull
  private String title;
  @NotNull
  private String content;

  public static NewsCreateCommand create(
      String title,
      String content
  ) {
    return new NewsCreateCommand(
        UUID.randomUUID(),
        title,
        content
    );
  }
}
