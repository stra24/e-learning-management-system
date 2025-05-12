package com.everrefine.elms.application.command;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 更新用ユーザーのコマンド。
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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
    return new UserUpdateCommand(
        id,
        realName,
        userName,
        emailAddress,
        thumbnailUrl,
        LocalDateTime.now()
    );
  }
}