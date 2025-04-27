package com.everrefine.elms.domain.model.user;

import java.util.regex.Pattern;
import lombok.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * パスワードの値オブジェクト。
 */
@Value
public class Password {

  // BCrypt形式であることを確認する正規表現
  private static final Pattern BCRYPT_PATTERN = Pattern.compile(
      "^\\$2[aby]?\\$\\d{2}\\$[./A-Za-z0-9]{53}$"
  );
  String value;

  public Password(String value) {
    if (value == null || !BCRYPT_PATTERN.matcher(value).matches()) {
      throw new IllegalArgumentException("Password must be a valid BCrypt hash.");
    }
    this.value = value;
  }

  public static Password encryptAndCreate(String value) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    return new Password(encoder.encode(value));
  }
}