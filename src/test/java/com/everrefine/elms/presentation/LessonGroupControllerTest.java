package com.everrefine.elms.presentation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.everrefine.elms.application.dto.LessonGroupDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * {@link LessonGroupController}の統合テストクラス。
 * 
 * <p>このテストクラスでは、実際のHTTPリクエストとレスポンスをテストします。
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Testcontainers // DBはDockerコンテナを使用する。
public class LessonGroupControllerTest {

  /**
   * テストで使うDBを用意する。
   */
  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgres =
      new PostgreSQLContainer<>("postgres:17");

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  private String baseUrl;
  private Integer courseId;
  private Integer lessonGroupId;

  @BeforeEach
  void setUp() {
    baseUrl = "http://localhost:" + port + "/api/courses";
    
    // テストデータをクリーンアップ
    jdbcTemplate.execute("DELETE FROM lesson_groups");
    jdbcTemplate.execute("DELETE FROM courses");
    
    // テストデータを準備
    jdbcTemplate.update(
        "INSERT INTO courses (course_order, title, description, created_at, updated_at) VALUES (?, ?, ?, ?, ?)",
        new BigDecimal("1"), "テストコース", "コース説明", LocalDateTime.now(), LocalDateTime.now());
    courseId = jdbcTemplate.queryForObject("SELECT id FROM courses WHERE title = ?", Integer.class, "テストコース");
    
    jdbcTemplate.update(
        "INSERT INTO lesson_groups (course_id, lesson_group_order, title, created_at, updated_at) VALUES (?, ?, ?, ?, ?)",
        courseId, new BigDecimal("1"), "元のレッスングループ", LocalDateTime.now(), LocalDateTime.now());
    lessonGroupId = jdbcTemplate.queryForObject("SELECT id FROM lesson_groups WHERE title = ?", Integer.class, "元のレッスングループ");
  }

  @Test
  void 正常系_レッスングループを更新できること() {
    // Arrange
    String requestBody = """
        {
          "title": "更新後のレッスングループ"
        }
        """;

    HttpHeaders headers = new HttpHeaders();
    headers.set("Content-Type", "application/json");
    HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

    // Act
    ResponseEntity<LessonGroupDto> response = restTemplate.exchange(
        baseUrl + "/" + courseId + "/lesson-groups/" + lessonGroupId,
        HttpMethod.PUT,
        request,
        LessonGroupDto.class
    );

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(lessonGroupId, response.getBody().id());
    assertEquals(courseId, response.getBody().courseId());
    assertEquals("更新後のレッスングループ", response.getBody().name());
    assertTrue(response.getBody().updatedAt().isAfter(response.getBody().createdAt()));

    // データベースの検証
    String dbTitle = jdbcTemplate.queryForObject(
        "SELECT title FROM lesson_groups WHERE id = ?", String.class, lessonGroupId);
    assertEquals("更新後のレッスングループ", dbTitle);
  }

  @Test
  void 異常系_存在しないレッスングループIDで404エラーが返ること() {
    // Arrange
    String requestBody = """
        {
          "title": "更新後のタイトル"
        }
        """;

    HttpHeaders headers = new HttpHeaders();
    headers.set("Content-Type", "application/json");
    HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

    // Act
    ResponseEntity<String> response = restTemplate.exchange(
        baseUrl + "/" + courseId + "/lesson-groups/999",
        HttpMethod.PUT,
        request,
        String.class
    );

    // Assert
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void 異常系_異なるコースIDで404エラーが返ること() {
    // Arrange
    // 別のコースを作成
    jdbcTemplate.update(
        "INSERT INTO courses (course_order, title, description, created_at, updated_at) VALUES (?, ?, ?, ?, ?)",
        new BigDecimal("2"), "別のコース", "別の説明", LocalDateTime.now(), LocalDateTime.now());
    Integer otherCourseId = jdbcTemplate.queryForObject("SELECT id FROM courses WHERE title = ?", Integer.class, "別のコース");

    String requestBody = """
        {
          "title": "更新後のタイトル"
        }
        """;

    HttpHeaders headers = new HttpHeaders();
    headers.set("Content-Type", "application/json");
    HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

    // Act
    ResponseEntity<String> response = restTemplate.exchange(
        baseUrl + "/" + otherCourseId + "/lesson-groups/" + lessonGroupId,
        HttpMethod.PUT,
        request,
        String.class
    );

    // Assert
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void 異常系_nullタイトルでバリデーションエラーが返ること() {
    // Arrange
    String requestBody = """
        {
          "title": null
        }
        """;

    HttpHeaders headers = new HttpHeaders();
    headers.set("Content-Type", "application/json");
    HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

    // Act
    ResponseEntity<String> response = restTemplate.exchange(
        baseUrl + "/" + courseId + "/lesson-groups/" + lessonGroupId,
        HttpMethod.PUT,
        request,
        String.class
    );

    // Assert
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertTrue(response.getBody().contains("レッスングループタイトルは必須です"));
  }

  @Test
  void 異常系_空文字タイトルでバリデーションエラーが返ること() {
    // Arrange
    String requestBody = """
        {
          "title": ""
        }
        """;

    HttpHeaders headers = new HttpHeaders();
    headers.set("Content-Type", "application/json");
    HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

    // Act
    ResponseEntity<String> response = restTemplate.exchange(
        baseUrl + "/" + courseId + "/lesson-groups/" + lessonGroupId,
        HttpMethod.PUT,
        request,
        String.class
    );

    // Assert
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertTrue(response.getBody().contains("レッスングループタイトルは必須です"));
  }

  @Test
  void 異常系_100文字を超えるタイトルでバリデーションエラーが返ること() {
    // Arrange
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 101; i++) {
      sb.append("a");
    }
    String requestBody = String.format("""
        {
          "title": "%s"
        }
        """, sb.toString());

    HttpHeaders headers = new HttpHeaders();
    headers.set("Content-Type", "application/json");
    HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

    // Act
    ResponseEntity<String> response = restTemplate.exchange(
        baseUrl + "/" + courseId + "/lesson-groups/" + lessonGroupId,
        HttpMethod.PUT,
        request,
        String.class
    );

    // Assert
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertTrue(response.getBody().contains("タイトルは100文字以内で入力してください"));
  }

  @Test
  void 正常系_100文字ちょうどのタイトルで更新できること() {
    // Arrange
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 100; i++) {
      sb.append("a");
    }
    String requestBody = String.format("""
        {
          "title": "%s"
        }
        """, sb.toString());

    HttpHeaders headers = new HttpHeaders();
    headers.set("Content-Type", "application/json");
    HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

    // Act
    ResponseEntity<LessonGroupDto> response = restTemplate.exchange(
        baseUrl + "/" + courseId + "/lesson-groups/" + lessonGroupId,
        HttpMethod.PUT,
        request,
        LessonGroupDto.class
    );

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(100, response.getBody().name().length());
  }
}
