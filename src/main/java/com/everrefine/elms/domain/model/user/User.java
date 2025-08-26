package com.everrefine.elms.domain.model.user;

import com.everrefine.elms.domain.model.Url;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.Nullable;

/**
 * ユーザーのエンティティ。
 */
@Getter
@AllArgsConstructor
@Table("users")
public class User {

  @Id
  private final UUID id;
  @NotNull
  @Column("email_address")
  private EmailAddress emailAddress;
  @NotNull
  private Password password;
  @NotNull
  @Column("real_name")
  private RealName realName;
  @NotNull
  @Column("user_name")
  private UserName userName;
  @Nullable
  @Column("thumbnail_url")
  private Url thumbnailUrl;
  @NotNull
  @Column("user_role")
  private UserRole userRole;
  @NotNull
  @Column("created_at")
  private LocalDateTime createdAt;
  @NotNull
  @Column("updated_at")
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