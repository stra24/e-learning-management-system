package com.everrefine.elms.presentation.request;

import lombok.Data;

@Data
public class LoginRequest {
  private String emailAddress;
  private String password;
}