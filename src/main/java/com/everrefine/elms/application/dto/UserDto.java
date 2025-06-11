package com.everrefine.elms.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserDto {
  private final UUID id;
  private final String emailAddress;
  private final String realName;
  private final String userName;
  private final String thumbnailUrl;
  private final String userRole;
  private final LocalDateTime createdAt;
}