package com.everrefine.elms.application.dto;

import com.everrefine.elms.domain.model.user.User;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;

@Getter
public class UserDto {

  private final UUID id;
  private final String emailAddress;
  private final String realName;
  private final String userName;
  private final String thumbnailUrl;
  private final String userRole;
  private final LocalDateTime createdAt;

  // コンストラクタ
  public UserDto(User user) {
    this.id = user.getId();
    this.emailAddress = user.getEmailAddress().getValue();
    this.realName = user.getRealName().getValue();
    this.userName = user.getUserName().getValue();
    this.thumbnailUrl = (user.getThumbnailUrl() != null)
        ? user.getThumbnailUrl().getValue()
        : null;
    this.userRole = user.getUserRole().getRoleName();
    this.createdAt = user.getCreatedAt();
  }
}