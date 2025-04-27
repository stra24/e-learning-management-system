package com.everrefine.elms.domain.model.user;

public enum UserRole {
  GENERAL("一般"),
  ADMIN("管理者");

  private final String roleName;

  UserRole(String roleName) {
    this.roleName = roleName;
  }

  public String getRoleName() {
    return roleName;
  }

  public String getCode() {
    return name();
  }
}
