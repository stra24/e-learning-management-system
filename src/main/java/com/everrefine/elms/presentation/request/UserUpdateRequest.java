package com.everrefine.elms.presentation.request;

import lombok.Data;

@Data
public class UserUpdateRequest {

  private String userId;
  private String realName;
  private String userName;
  private String emailAddress;
  private String thumbnailUrl;
}