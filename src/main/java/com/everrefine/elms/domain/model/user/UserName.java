package com.everrefine.elms.domain.model.user;

import java.util.regex.Pattern;
import lombok.Value;

/**
 * ユーザー名の値オブジェクト。
 */
@Value
public class UserName {

  // 最大文字数
  private static final int MAX_LENGTH = 50;
  // 全角文字が含まれていないことを確認する正規表現
  private static final Pattern NON_FULL_WIDTH_PATTERN = Pattern.compile("^[\u0000-\u00ff]*$");
  String value;

  public UserName(String value) {
    if (value == null || value.length() > MAX_LENGTH) {
      throw new IllegalArgumentException(
          "UserName must be " + MAX_LENGTH + " characters or fewer.");
    }

    if (!NON_FULL_WIDTH_PATTERN.matcher(value).matches()) {
      throw new IllegalArgumentException("UserName must not contain full-width characters.");
    }

    this.value = value;
  }
}