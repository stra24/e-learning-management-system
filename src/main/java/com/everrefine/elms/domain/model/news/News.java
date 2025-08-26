package com.everrefine.elms.domain.model.news;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * お知らせのエンティティ。
 */
@Getter
@AllArgsConstructor
@Table("news")
public class News {

  @Id
  private final UUID id;
  @NotNull
  private Title title;
  @NotNull
  private Content content;
  @NotNull
  @Column("created_at")
  private LocalDateTime createdAt;
  @NotNull
  @Column("updated_at")
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