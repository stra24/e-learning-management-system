package com.everrefine.elms.application.dto;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NewsDto {
  private final UUID id;
  private final String title;
  private final String content;
  private final LocalDate createdAt;
  private final LocalDate updatedAt;
}