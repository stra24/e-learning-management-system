package com.everrefine.elms.application.dto;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CourseDto {

  private final UUID id;
  private final BigDecimal courseOrder;
  private final String thumbnailUrl;
  private final String title;
  private final String description;
}