package com.everrefine.elms.application.command;

import jakarta.annotation.Nullable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "create")
public class UserSearchCommand {

  private int pageNum;
  private int pageSize;
  @Nullable
  private String userId;
  @Nullable
  private String userRole;
  @Nullable
  private String realName;
  @Nullable
  private String userName;
  @Nullable
  private String emailAddress;
  @Nullable
  LocalDate createdDateFrom;
  @Nullable
  LocalDate createdDateTo;
}

