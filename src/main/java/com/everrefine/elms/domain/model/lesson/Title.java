package com.everrefine.elms.domain.model.lesson;

import lombok.Value;

/**
 * レッスン名の値オブジェクト。
 */
@Value
public class Title {

  // 最大文字数
  private static final int MAX_LENGTH = 255;
  String value;

  public Title(String value) {
    if (value == null || value.length() > MAX_LENGTH) {
      throw new IllegalArgumentException(
          "Title must be " + MAX_LENGTH + " characters or fewer.");
    }
    this.value = value;
  }
}
