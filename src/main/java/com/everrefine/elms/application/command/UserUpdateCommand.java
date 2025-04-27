package com.everrefine.elms.application.command;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 更新用ユーザーのエンティティ。
 */
@Getter
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class UserUpdateCommand {

  @NotNull
  private UUID id;
  @NotNull
  private String realName;
  @NotNull
  private String userName;
  @NotNull
  private String emailAddress;
  @Nullable
  private String thumbnailUrl;
  @NotNull
  private LocalDateTime updatedAt;

  public static UserUpdateCommand create(
      UUID id,
      String realName,
      String userName,
      String emailAddress,
      String thumbnailUrl
  ) {
    LocalDateTime now = LocalDateTime.now();
    return new UserUpdateCommand(
        id,
        realName,
        userName,
        emailAddress,
        thumbnailUrl,
        now
    );
  }
}