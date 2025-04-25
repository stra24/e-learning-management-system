package com.everrefine.elms.domain.model.course;

import com.everrefine.elms.domain.model.Url;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * コースのエンティティ。
 */
@Getter
@AllArgsConstructor
public class Course {

  @NotNull
  private final UUID id;
  @Nullable
  private Url thumbnailUrl;
  @NotNull
  private Title title;
  @Nullable
  private Description description;
  @NotNull
  private LocalDateTime createdAt;
  @NotNull
  private LocalDateTime updatedAt;

  public void changeThumbnailUrl(Url newThumbnailUrl) {
    this.thumbnailUrl = newThumbnailUrl;
    this.updatedAt = LocalDateTime.now();
  }

  public void changeTitle(Title newTitle) {
    this.title = newTitle;
    this.updatedAt = LocalDateTime.now();
  }

  public void changeDescription(Description newDescription) {
    this.description = newDescription;
    this.updatedAt = LocalDateTime.now();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Course other)) {
      return false;
    }
    return id.equals(other.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}