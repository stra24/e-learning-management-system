package com.everrefine.elms.presentation.request;

import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LessonGroupUpdateRequestTest {

  private ValidatorFactory validatorFactory;
  private Validator validator;

  @BeforeEach
  void setUp() {
    validatorFactory = Validation.buildDefaultValidatorFactory();
    validator = validatorFactory.getValidator();
  }

  @AfterEach
  void tearDown() {
    validatorFactory.close();
  }

  @Test
  void 正常系_有効なタイトルでバリデーションが通ること() {
    // Arrange
    LessonGroupUpdateRequest request = new LessonGroupUpdateRequest();
    request.setTitle("有効なタイトル");

    // Act
    Set<ConstraintViolation<LessonGroupUpdateRequest>> violations = validator.validate(request);

    // Assert
    assertEquals(0, violations.size());
  }

  @Test
  void 異常系_nullタイトルでバリデーションエラーが発生すること() {
    // Arrange
    LessonGroupUpdateRequest request = new LessonGroupUpdateRequest();
    request.setTitle(null);

    // Act
    Set<ConstraintViolation<LessonGroupUpdateRequest>> violations = validator.validate(request);

    // Assert
    assertEquals(1, violations.size());
    assertEquals("レッスングループタイトルは必須です", violations.iterator().next().getMessage());
  }

  @Test
  void 異常系_空文字タイトルでバリデーションエラーが発生すること() {
    // Arrange
    LessonGroupUpdateRequest request = new LessonGroupUpdateRequest();
    request.setTitle("");

    // Act
    Set<ConstraintViolation<LessonGroupUpdateRequest>> violations = validator.validate(request);

    // Assert
    assertEquals(1, violations.size());
    assertEquals("レッスングループタイトルは必須です", violations.iterator().next().getMessage());
  }

  @Test
  void 異常系_空白のみのタイトルでバリデーションエラーが発生すること() {
    // Arrange
    LessonGroupUpdateRequest request = new LessonGroupUpdateRequest();
    request.setTitle("   ");

    // Act
    Set<ConstraintViolation<LessonGroupUpdateRequest>> violations = validator.validate(request);

    // Assert
    assertEquals(1, violations.size());
    assertEquals("レッスングループタイトルは必須です", violations.iterator().next().getMessage());
  }

  @Test
  void 異常系_100文字を超えるタイトルでバリデーションエラーが発生すること() {
    // Arrange
    LessonGroupUpdateRequest request = new LessonGroupUpdateRequest();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 101; i++) {
      sb.append("a");
    }
    request.setTitle(sb.toString());

    // Act
    Set<ConstraintViolation<LessonGroupUpdateRequest>> violations = validator.validate(request);

    // Assert
    assertEquals(1, violations.size());
    assertEquals("タイトルは100文字以内で入力してください", violations.iterator().next().getMessage());
  }

  @Test
  void 正常系_100文字ちょうどのタイトルでバリデーションが通ること() {
    // Arrange
    LessonGroupUpdateRequest request = new LessonGroupUpdateRequest();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 100; i++) {
      sb.append("a");
    }
    request.setTitle(sb.toString());

    // Act
    Set<ConstraintViolation<LessonGroupUpdateRequest>> violations = validator.validate(request);

    // Assert
    assertEquals(0, violations.size());
  }
}
