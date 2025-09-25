package com.everrefine.elms.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CourseDto {

  private final Integer id;
  private final BigDecimal courseOrder;
  private final String thumbnailUrl;
  private final String title;
  private final String description;
  private final LocalDateTime createdAt;
  private final LocalDateTime updatedAt;
}