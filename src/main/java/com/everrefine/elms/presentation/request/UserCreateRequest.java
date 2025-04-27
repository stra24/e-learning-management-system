package com.everrefine.elms.presentation.request;

import com.everrefine.elms.domain.model.user.UserRole;
import lombok.Data;

@Data
public class UserCreateRequest {

  private String realName;
  private String userName;
  private String emailAddress;
  private String password;
  private String confirmPassword;
  private String thumbnailUrl;
  private UserRole userRole;
}