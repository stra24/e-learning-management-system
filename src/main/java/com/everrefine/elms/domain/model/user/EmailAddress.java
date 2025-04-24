package com.everrefine.elms.domain.model.user;

import java.util.regex.Pattern;
import lombok.Value;

/**
 * Eメールアドレスの値オブジェクト。
 */
@Value
public class EmailAddress {

  // Eメールアドレス形式であることを確認する正規表現
  private static final Pattern EMAIL_PATTERN = Pattern.compile(
      "^[\\w\\.-]+@[\\w\\.-]+\\.[a-zA-Z]{2,}$"
  );
  String value;

  public EmailAddress(String value) {
    if (value == null || !EMAIL_PATTERN.matcher(value).matches()) {
      throw new IllegalArgumentException("Invalid email address: " + value);
    }
    this.value = value;
  }
}