package com.everrefine.elms.presentation.request;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class CourseUpdateRequest {

  private Integer courseId;
  private BigDecimal courseOrder;
  private String title;
  private String description;
  private String thumbnailUrl;
}