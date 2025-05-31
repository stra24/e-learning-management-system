package com.everrefine.elms.presentation.request;

import java.time.LocalDate;
import lombok.Data;

@Data
public class NewsSearchRequest {

  private int pageNum = 1;
  private int pageSize = 10;
  private String title;
  private LocalDate createdDateFrom;
  private LocalDate createdDateTo;
}

