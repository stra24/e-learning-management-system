package com.everrefine.elms.application.command;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NewsSearchCommand {

  private int pageNum;

  private int pageSize;

  @NotNull
  private String title;

  @NotNull
  private LocalDate createdDateFrom;

  @NotNull
  private LocalDate createDateTo;

  public static NewsSearchCommand create(
      int pageNum,
      int pageSize,
      String title,
      LocalDate createDateFrom,
      LocalDate createDateTo
  ) {
    return new NewsSearchCommand(
        pageNum,
        pageSize,
        title,
        createDateFrom,
        createDateTo);
  }


}

