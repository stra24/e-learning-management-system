package com.everrefine.elms.presentation.request;

import jakarta.annotation.Nullable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class UserSearchRequest {

  private int pageNum = 1;
  private int pageSize = 10;

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

