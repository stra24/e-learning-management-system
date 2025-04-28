package com.everrefine.elms.application.command;

import com.everrefine.elms.domain.model.user.UserRole;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 新規作成用ユーザーのコマンド。
 */
@Getter
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class UserCreateCommand {

  @NotNull
  private UUID id;
  @NotNull
  private String realName;
  @NotNull
  private String userName;
  @NotNull
  private String emailAddress;
  @NotNull
  private String password;
  @NotNull
  private String confirmPassword;
  @Nullable
  private String thumbnailUrl;
  @NotNull
  private UserRole userRole;

  public static UserCreateCommand create(
      String realName,
      String userName,
      String emailAddress,
      String password,
      String confirmPassword,
      String thumbnailUrl,
      UserRole userRole
  ) {
    return new UserCreateCommand(
        UUID.randomUUID(),
        realName,
        userName,
        emailAddress,
        password,
        confirmPassword,
        thumbnailUrl,
        userRole
    );
  }
}