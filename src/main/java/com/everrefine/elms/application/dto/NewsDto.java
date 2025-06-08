package com.everrefine.elms.application.dto;

import com.everrefine.elms.domain.model.news.News;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Getter;

@Getter
public class NewsDto {

  private final UUID id;
  private final String title;
  private final String content;
  private final LocalDate createdAt;
  private final LocalDate updatedAt;

  // コンストラクタ
  public NewsDto(News news) {
    this.id = news.getId();
    this.title = news.getTitle().getValue();
    this.content = news.getContent().getValue();
    this.createdAt = news.getCreatedAt().toLocalDate();
    this.updatedAt = news.getUpdatedAt().toLocalDate();
  }
}