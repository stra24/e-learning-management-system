package com.everrefine.elms.domain.model.lesson;

import lombok.Value;

/**
 * コース説明の値オブジェクト。
 */
@Value
public class Description {

  // 最大文字数
  private static final int MAX_LENGTH = 1_000_000;
  String value;

  public Description(String value) {
    if (value == null || value.length() > MAX_LENGTH) {
      throw new IllegalArgumentException(
          "Description must be " + MAX_LENGTH + " characters or fewer.");
    }
    this.value = value;
  }
}