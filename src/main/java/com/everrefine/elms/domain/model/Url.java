package com.everrefine.elms.domain.model;

import java.net.URI;
import java.net.URISyntaxException;
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

    // URI形式が妥当かをチェック
    try {
      URI uri = new URI(value);

      // スキームの制限（http, https）
      String scheme = uri.getScheme();
      if (scheme == null || !(scheme.equals("http") || scheme.equals("https"))) {
        throw new IllegalArgumentException("Only http and https schemes are allowed");
      }

      // ホストの存在チェック（nullの場合は不完全なURL）
      if (uri.getHost() == null) {
        throw new IllegalArgumentException("Invalid URL: host is missing");
      }

    } catch (URISyntaxException e) {
      throw new IllegalArgumentException("Invalid URL format", e);
    }

    this.value = value;
  }

  @Override
  public String toString() {
    return value;
  }
}