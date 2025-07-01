package com.everrefine.elms.presentation.request;

import jakarta.annotation.Nullable;
import java.time.LocalDate;
import lombok.Data;

@Data
public class NewsSearchRequest {

  private int pageNum = 1;
  private int pageSize = 10;

  @Nullable
  private String title;

  @Nullable
  private LocalDate createdDateFrom;

  @Nullable
  private LocalDate createdDateTo;
}

