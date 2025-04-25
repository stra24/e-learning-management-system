package com.everrefine.elms.domain.model.news;

import lombok.Value;

/**
 * お知らせ本文の値オブジェクト。
 */
@Value
public class Content {

  // 最大文字数
  private static final int MAX_LENGTH = 1_000_000;
  String value;

  public Content(String value) {
    if (value == null || value.length() > MAX_LENGTH) {
      throw new IllegalArgumentException(
          "Content must be " + MAX_LENGTH + " characters or fewer.");
    }
    this.value = value;
  }
}