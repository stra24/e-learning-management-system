package com.everrefine.elms.domain.model.user;

import com.everrefine.elms.domain.model.Url;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ユーザーの更新用リクエストのエンティティ。
 */
@Getter
@AllArgsConstructor
public class UserForUpdateRequest {

  @NotNull
  private final Integer id;
  @NotNull
  private EmailAddress emailAddress;
  @NotNull
  private RealName realName;
  @NotNull
  private UserName userName;
  @Nullable
  private Url thumbnailUrl;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof UserForUpdateRequest other)) {
      return false;
    }
    return id.equals(other.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}