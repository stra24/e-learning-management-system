package com.everrefine.elms.domain.model;

import lombok.Value;

/**
 * URLの値オブジェクト。 画像や動画のURLとして使うことを想定している。
 */
@Value
public class Url {

  public static final int MAX_LENGTH = 2048;

  String value;

  public Url(String value) {
    if (value == null) {
      throw new IllegalArgumentException("URL must not be null");
    }

    if (value.length() > MAX_LENGTH) {
      throw new IllegalArgumentException(
          "URL must be " + MAX_LENGTH + " characters or less");
    }

    this.value = value;
  }

  @Override
  public String toString() {
    return value;
  }
}