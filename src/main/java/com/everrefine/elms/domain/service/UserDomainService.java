package com.everrefine.elms.domain.service;

import org.springframework.stereotype.Component;

@Component
public class UserDomainService {

  public static boolean matchesPassword(String password, String confirmPassword) {
    return password != null
        && password.equals(confirmPassword);
  }
}