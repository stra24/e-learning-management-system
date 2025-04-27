package com.everrefine.elms.domain.model.user;

import com.everrefine.elms.domain.model.Url;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ユーザーのエンティティ。
 */
@Getter
@AllArgsConstructor
public class User {

  @NotNull
  private final UUID id;
  @NotNull
  private EmailAddress emailAddress;
  @NotNull
  private Password password;
  @NotNull
  private RealName realName;
  @NotNull
  private UserName userName;
  @Nullable
  private Url thumbnailUrl;
  @NotNull
  private UserRole userRole;
  @NotNull
  private LocalDateTime createdAt;
  @NotNull
  private LocalDateTime updatedAt;

  public void changeEmailAddress(EmailAddress newEmailAddress) {
    this.emailAddress = newEmailAddress;
    this.updatedAt = LocalDateTime.now();
  }

  public void changePassword(Password newPassword) {
    this.password = newPassword;
    this.updatedAt = LocalDateTime.now();
  }

  public void changeRealName(RealName newRealName) {
    this.realName = newRealName;
    this.updatedAt = LocalDateTime.now();
  }

  public void changeUserName(UserName newUserName) {
    this.userName = newUserName;
    this.updatedAt = LocalDateTime.now();
  }

  public void changeThumbnailUrl(Url newThumbnailUrl) {
    this.thumbnailUrl = newThumbnailUrl;
    this.updatedAt = LocalDateTime.now();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof User other)) {
      return false;
    }
    return id.equals(other.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}