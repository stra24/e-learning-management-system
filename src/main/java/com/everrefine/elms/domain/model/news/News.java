package com.everrefine.elms.domain.model.news;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * お知らせのエンティティ。
 */
@Getter
@AllArgsConstructor
public class News {

  @NotNull
  private final UUID id;
  @NotNull
  private Title title;
  @NotNull
  private Content content;
  @NotNull
  private LocalDateTime createdAt;
  @NotNull
  private LocalDateTime updatedAt;

  public void changeTitle(Title newTitle) {
    this.title = newTitle;
    this.updatedAt = LocalDateTime.now();
  }

  public void changeContent(Content newContent) {
    this.content = newContent;
    this.updatedAt = LocalDateTime.now();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof News other)) {
      return false;
    }
    return id.equals(other.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}