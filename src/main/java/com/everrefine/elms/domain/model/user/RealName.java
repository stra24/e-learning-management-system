package com.everrefine.elms.domain.model.user;

import lombok.Value;

/**
 * 氏名の値オブジェクト。
 */
@Value
public class RealName {

  // 最大文字数
  private static final int MAX_LENGTH = 50;
  String value;

  public RealName(String value) {
    if (value == null || value.length() > MAX_LENGTH) {
      throw new IllegalArgumentException(
          "RealName must be " + MAX_LENGTH + " characters or fewer.");
    }
    this.value = value;
  }
}