package com.everrefine.elms.domain.model.news;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NewsForUpdateRequest {

  @NotNull
  private final Integer id;
  @NotNull
  private Title title;
  @NotNull
  private Content content;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof NewsForUpdateRequest other)) {
      return false;
    }
    return id.equals(other.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}
