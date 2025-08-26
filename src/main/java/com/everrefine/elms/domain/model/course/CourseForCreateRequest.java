package com.everrefine.elms.domain.model.course;

import com.everrefine.elms.domain.model.Url;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * コースの新規作成用リクエストのエンティティ。
 */
@Getter
@AllArgsConstructor
public class CourseForCreateRequest {

  private final UUID id;
  @NotNull
  private Title title;
  @NotNull
  private Description description;
  @Nullable
  private Url thumbnailUrl;
  @NotNull
  private LocalDateTime createdAt;
  @NotNull
  private LocalDateTime updatedAt;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof CourseForCreateRequest other)) {
      return false;
    }
    return id.equals(other.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}