package com.everrefine.elms.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.everrefine.elms.application.command.LessonCreateCommand;
import com.everrefine.elms.application.command.LessonSearchCommand;
import com.everrefine.elms.application.command.LessonUpdateCommand;
import com.everrefine.elms.application.dto.CourseLessonsDto;
import com.everrefine.elms.application.dto.FirstLessonDto;
import com.everrefine.elms.application.dto.LessonDto;
import com.everrefine.elms.application.dto.LessonPageDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * {@link LessonApplicationServiceImpl}の統合テストクラス。
 * 
 * <p>このテストクラスでは、実際のPostgreSQLデータベースを使用した統合テストを実施します。
 * Testcontainersを利用してテスト用のDockerコンテナを起動し、Spring Bootのテスト機能と組み合わせることで、
 * 実運用環境に近い状態でのテストを保証します。
 * 
 * <p>主な検証項目：
 * <ul>
 *   <li>実運用に近いシナリオの網羅</li>
 *   <li>エッジケースと例外処理の検証</li>
 *   <li>実際のデータベース操作の検証</li>
 *   <li>ビジネスロジックの正確性</li>
 *   <li>トランザクション境界の考慮</li>
 * </ul>
 */
@SpringBootTest(webEnvironment = WebEnvironment.NONE) // WebまわりのConfigurationはBean生成を無効にして高速化する。
@Testcontainers // DBはDockerコンテナを使用する。
@Transactional // 各テストメソッド終了時にテストデータをロールバックする。
public class LessonApplicationServiceImplTest {

  /**
   * テストで使うDBを用意する。
   */
  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgres =
      new PostgreSQLContainer<>("postgres:17");

  /**
   * テスト対象のサービスクラス。
   */
  @Autowired
  private LessonApplicationServiceImpl lessonApplicationService;

  /**
   * データ検証で使用するためのJdbcTemplate。
   */
  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Test
  void 正常系_最初のレッスンIDを取得できること() {
    // Arrange - テストデータを準備（IDは自動生成）
    jdbcTemplate.update(
        "INSERT INTO courses (course_order, title, description, created_at, updated_at) VALUES (?, ?, ?, ?, ?)",
        new BigDecimal("1"), "テストコース", "コース説明", LocalDateTime.now(), LocalDateTime.now());
    Integer courseId = jdbcTemplate.queryForObject("SELECT id FROM courses WHERE title = ?", Integer.class, "テストコース");
    
    jdbcTemplate.update(
        "INSERT INTO lesson_groups (course_id, lesson_group_order, title, created_at, updated_at) VALUES (?, ?, ?, ?, ?)",
        courseId, new BigDecimal("1"), "テストグループ", LocalDateTime.now(), LocalDateTime.now());
    Integer lessonGroupId = jdbcTemplate.queryForObject("SELECT id FROM lesson_groups WHERE title = ?", Integer.class, "テストグループ");
    
    jdbcTemplate.update(
        "INSERT INTO lessons (lesson_group_id, course_id, lesson_order, title, description, video_url, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
        lessonGroupId, courseId, new BigDecimal("1"), "テストレッスン", "テスト説明", "https://example.com/video.mp4", 
        LocalDateTime.now(), LocalDateTime.now());

    // Act
    FirstLessonDto result = lessonApplicationService.findFirstLessonIdByCourseId(courseId);

    // Assert
    assertTrue(result.existsLesson());
    assertEquals(lessonGroupId, result.firstLessonGroupId());
    assertNotNull(result.firstLessonId());
  }

  @Test
  void 正常系_レッスンが存在しない場合falseを返すこと() {
    // Arrange - レッスンが存在しないコースID
    jdbcTemplate.update(
        "INSERT INTO courses (course_order, title, description, created_at, updated_at) VALUES (?, ?, ?, ?, ?)",
        new BigDecimal("1"), "空のコース", "説明", LocalDateTime.now(), LocalDateTime.now());
    Integer courseId = jdbcTemplate.queryForObject("SELECT id FROM courses WHERE title = ?", Integer.class, "空のコース");

    // Act
    FirstLessonDto result = lessonApplicationService.findFirstLessonIdByCourseId(courseId);

    // Assert
    assertFalse(result.existsLesson());
    assertNull(result.firstLessonGroupId());
    assertNull(result.firstLessonId());
  }

  @Test
  void 正常系_courseIdがnullの場合falseを返すこと() {
    // Act
    FirstLessonDto result = lessonApplicationService.findFirstLessonIdByCourseId(null);

    // Assert
    assertFalse(result.existsLesson());
    assertNull(result.firstLessonGroupId());
    assertNull(result.firstLessonId());
  }

  @Test
  void 正常系_レッスンをIDで取得できること() {
    // Arrange - テストデータを準備（IDは自動生成）
    jdbcTemplate.update(
        "INSERT INTO courses (course_order, title, description, created_at, updated_at) VALUES (?, ?, ?, ?, ?)",
        new BigDecimal("1"), "テストコース", "コース説明", LocalDateTime.now(), LocalDateTime.now());
    Integer courseId = jdbcTemplate.queryForObject("SELECT id FROM courses WHERE title = ?", Integer.class, "テストコース");
    
    jdbcTemplate.update(
        "INSERT INTO lesson_groups (course_id, lesson_group_order, title, created_at, updated_at) VALUES (?, ?, ?, ?, ?)",
        courseId, new BigDecimal("1"), "テストグループ", LocalDateTime.now(), LocalDateTime.now());
    Integer lessonGroupId = jdbcTemplate.queryForObject("SELECT id FROM lesson_groups WHERE title = ?", Integer.class, "テストグループ");
    
    jdbcTemplate.update(
        "INSERT INTO lessons (lesson_group_id, course_id, lesson_order, title, description, video_url, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
        lessonGroupId, courseId, new BigDecimal("1"), "テストレッスン", "テスト説明", "https://example.com/video.mp4", 
        LocalDateTime.now(), LocalDateTime.now());

    // 生成されたIDを取得
    Integer lessonId = jdbcTemplate.queryForObject(
        "SELECT id FROM lessons WHERE title = ?", Integer.class, "テストレッスン");

    // Act
    LessonDto result = lessonApplicationService.findLessonById(courseId, lessonId);

    // Assert
    assertNotNull(result);
    assertEquals(lessonId, result.getId());
    assertEquals(lessonGroupId, result.getLessonGroupId());
    assertEquals(courseId, result.getCourseId());
    assertEquals(new BigDecimal("1.0000"), result.getLessonOrder());
    assertEquals("テストレッスン", result.getTitle());
    assertEquals("テスト説明", result.getDescription());
    assertEquals("https://example.com/video.mp4", result.getVideoUrl());
    assertNotNull(result.getCreatedAt());
    assertNotNull(result.getUpdatedAt());
  }

  @Test
  void 異常系_存在しないレッスンIDで例外が発生すること() {
    // Act & Assert
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> lessonApplicationService.findLessonById(1, 999)
    );
    assertEquals("Lesson not found", exception.getMessage());
  }

  @Test
  void 正常系_レッスンを検索できること() {
    // Arrange - テストデータを準備（IDは自動生成）
    jdbcTemplate.update(
        "INSERT INTO courses (course_order, title, description, created_at, updated_at) VALUES (?, ?, ?, ?, ?)",
        new BigDecimal("1"), "テストコース", "コース説明", LocalDateTime.now(), LocalDateTime.now());
    Integer courseId = jdbcTemplate.queryForObject("SELECT id FROM courses WHERE title = ?", Integer.class, "テストコース");
    
    jdbcTemplate.update(
        "INSERT INTO lesson_groups (course_id, lesson_group_order, title, created_at, updated_at) VALUES (?, ?, ?, ?, ?)",
        courseId, new BigDecimal("1"), "テストグループ", LocalDateTime.now(), LocalDateTime.now());
    Integer lessonGroupId = jdbcTemplate.queryForObject("SELECT id FROM lesson_groups WHERE title = ?", Integer.class, "テストグループ");
    
    jdbcTemplate.update(
        "INSERT INTO lessons (lesson_group_id, course_id, lesson_order, title, description, video_url, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
        lessonGroupId, courseId, new BigDecimal("1"), "テストレッスン", "テスト説明", "https://example.com/video.mp4", 
        LocalDateTime.now(), LocalDateTime.now());

    LessonSearchCommand searchCommand = LessonSearchCommand.create(1, 10, String.valueOf(courseId), null, null, null, null);

    // Act
    LessonPageDto result = lessonApplicationService.findLessons(searchCommand);

    // Assert
    assertNotNull(result);
    assertTrue(result.getTotalSize() >= 1);
    assertEquals(1, result.getPageNum());
    assertEquals(10, result.getPageSize());
  }

  @Test
  void 正常系_検索結果が0件の場合() {
    // Arrange - データが存在しない状態
    LessonSearchCommand searchCommand = LessonSearchCommand.create(1, 10, "999", null, null, null, null);

    // Act
    LessonPageDto result = lessonApplicationService.findLessons(searchCommand);

    // Assert
    assertNotNull(result);
    assertEquals(0, result.getTotalSize());
    assertEquals(1, result.getPageNum());
    assertEquals(10, result.getPageSize());
    assertTrue(result.getLessonDtos().isEmpty());
  }

  @Test
  void 正常系_コース別レッスン一覧を取得できること() {
    // Arrange - テストデータを準備（IDは自動生成）
    jdbcTemplate.update(
        "INSERT INTO courses (course_order, title, description, created_at, updated_at) VALUES (?, ?, ?, ?, ?)",
        new BigDecimal("1"), "テストコース", "コース説明", LocalDateTime.now(), LocalDateTime.now());
    Integer courseId = jdbcTemplate.queryForObject("SELECT id FROM courses WHERE title = ?", Integer.class, "テストコース");
    
    jdbcTemplate.update(
        "INSERT INTO lesson_groups (course_id, lesson_group_order, title, created_at, updated_at) VALUES (?, ?, ?, ?, ?)",
        courseId, new BigDecimal("1"), "テストグループ", LocalDateTime.now(), LocalDateTime.now());

    // Act
    CourseLessonsDto result = lessonApplicationService.findLessonsGroupedByLessonGroup(courseId);

    // Assert
    assertNotNull(result);
    assertNotNull(result.lessonGroups());
  }

  @Test
  void 正常系_レッスンを作成できること() {
    // Arrange - 関連データを準備（IDは自動生成）
    jdbcTemplate.update(
        "INSERT INTO courses (course_order, title, description, created_at, updated_at) VALUES (?, ?, ?, ?, ?)",
        new BigDecimal("1"), "テストコース", "コース説明", LocalDateTime.now(), LocalDateTime.now());
    Integer courseId = jdbcTemplate.queryForObject("SELECT id FROM courses WHERE title = ?", Integer.class, "テストコース");
    
    jdbcTemplate.update(
        "INSERT INTO lesson_groups (course_id, lesson_group_order, title, created_at, updated_at) VALUES (?, ?, ?, ?, ?)",
        courseId, new BigDecimal("1"), "テストグループ", LocalDateTime.now(), LocalDateTime.now());
    Integer lessonGroupId = jdbcTemplate.queryForObject("SELECT id FROM lesson_groups WHERE title = ?", Integer.class, "テストグループ");

    LessonCreateCommand command = LessonCreateCommand.create(
        courseId, lessonGroupId, "新規レッスン", "新規説明", "https://example.com/new-video.mp4");

    // Act
    LessonDto result = lessonApplicationService.createLesson(command);

    // Assert
    assertNotNull(result);
    assertEquals("新規レッスン", result.getTitle());
    assertEquals("新規説明", result.getDescription());
    assertEquals("https://example.com/new-video.mp4", result.getVideoUrl());
    
    // DBにデータが保存されていることを確認
    Integer count = jdbcTemplate.queryForObject(
        "SELECT COUNT(*) FROM lessons WHERE title = ?", Integer.class, "新規レッスン");
    assertEquals(1, count);
  }

  @Test
  void 正常系_null許容フィールドを指定せずにレッスンを作成できること() {
    // Arrange - 関連データを準備（IDは自動生成）
    jdbcTemplate.update(
        "INSERT INTO courses (course_order, title, description, created_at, updated_at) VALUES (?, ?, ?, ?, ?)",
        new BigDecimal("1"), "テストコース", "コース説明", LocalDateTime.now(), LocalDateTime.now());
    Integer courseId = jdbcTemplate.queryForObject("SELECT id FROM courses WHERE title = ?", Integer.class, "テストコース");
    
    jdbcTemplate.update(
        "INSERT INTO lesson_groups (course_id, lesson_group_order, title, created_at, updated_at) VALUES (?, ?, ?, ?, ?)",
        courseId, new BigDecimal("1"), "テストグループ", LocalDateTime.now(), LocalDateTime.now());
    Integer lessonGroupId = jdbcTemplate.queryForObject("SELECT id FROM lesson_groups WHERE title = ?", Integer.class, "テストグループ");

    LessonCreateCommand command = LessonCreateCommand.create(
        courseId, lessonGroupId, "最小構成レッスン", null, null);

    // Act
    LessonDto result = lessonApplicationService.createLesson(command);

    // Assert
    assertNotNull(result);
    assertEquals("最小構成レッスン", result.getTitle());
    assertNull(result.getDescription());
    assertNull(result.getVideoUrl());
  }

  @Test
  void 正常系_レッスンを更新できること() {
    // Arrange - 既存レッスンを準備（IDは自動生成）
    jdbcTemplate.update(
        "INSERT INTO courses (course_order, title, description, created_at, updated_at) VALUES (?, ?, ?, ?, ?)",
        new BigDecimal("1"), "テストコース", "コース説明", LocalDateTime.now(), LocalDateTime.now());
    Integer courseId = jdbcTemplate.queryForObject("SELECT id FROM courses WHERE title = ?", Integer.class, "テストコース");
    
    jdbcTemplate.update(
        "INSERT INTO lesson_groups (course_id, lesson_group_order, title, created_at, updated_at) VALUES (?, ?, ?, ?, ?)",
        courseId, new BigDecimal("1"), "テストグループ", LocalDateTime.now(), LocalDateTime.now());
    Integer lessonGroupId = jdbcTemplate.queryForObject("SELECT id FROM lesson_groups WHERE title = ?", Integer.class, "テストグループ");
    
    jdbcTemplate.update(
        "INSERT INTO lessons (lesson_group_id, course_id, lesson_order, title, description, video_url, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
        lessonGroupId, courseId, new BigDecimal("1"), "元のタイトル", "元の説明", "https://example.com/old-video.mp4", 
        LocalDateTime.now(), LocalDateTime.now());

    // 生成されたIDを取得
    Integer lessonId = jdbcTemplate.queryForObject(
        "SELECT id FROM lessons WHERE title = ?", Integer.class, "元のタイトル");

    LessonUpdateCommand command = LessonUpdateCommand.create(
        lessonId, "更新後タイトル", "更新後説明", "https://example.com/updated-video.mp4");

    // Act
    LessonDto result = lessonApplicationService.updateLesson(command);

    // Assert
    assertNotNull(result);
    assertEquals(lessonId, result.getId());
    assertEquals("更新後タイトル", result.getTitle());
    assertEquals("更新後説明", result.getDescription());
    assertEquals("https://example.com/updated-video.mp4", result.getVideoUrl());
    
    // DBが更新されていることを確認
    String updatedTitle = jdbcTemplate.queryForObject(
        "SELECT title FROM lessons WHERE id = ?", String.class, lessonId);
    assertEquals("更新後タイトル", updatedTitle);
  }

  @Test
  void 正常系_null許容フィールドをnullで更新できること() {
    // Arrange - 既存レッスンを準備（IDは自動生成）
    jdbcTemplate.update(
        "INSERT INTO courses (course_order, title, description, created_at, updated_at) VALUES (?, ?, ?, ?, ?)",
        new BigDecimal("1"), "テストコース", "コース説明", LocalDateTime.now(), LocalDateTime.now());
    Integer courseId = jdbcTemplate.queryForObject("SELECT id FROM courses WHERE title = ?", Integer.class, "テストコース");
    
    jdbcTemplate.update(
        "INSERT INTO lesson_groups (course_id, lesson_group_order, title, created_at, updated_at) VALUES (?, ?, ?, ?, ?)",
        courseId, new BigDecimal("1"), "テストグループ", LocalDateTime.now(), LocalDateTime.now());
    Integer lessonGroupId = jdbcTemplate.queryForObject("SELECT id FROM lesson_groups WHERE title = ?", Integer.class, "テストグループ");
    
    jdbcTemplate.update(
        "INSERT INTO lessons (lesson_group_id, course_id, lesson_order, title, description, video_url, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
        lessonGroupId, courseId, new BigDecimal("1"), "元のタイトル", "元の説明", "https://example.com/old-video.mp4", 
        LocalDateTime.now(), LocalDateTime.now());

    // 生成されたIDを取得
    Integer lessonId = jdbcTemplate.queryForObject(
        "SELECT id FROM lessons WHERE title = ?", Integer.class, "元のタイトル");

    // nullを渡すと元の値が保持される仕様
    LessonUpdateCommand command = LessonUpdateCommand.create(
        lessonId, "タイトルのみ更新", null, null);

    // Act
    LessonDto result = lessonApplicationService.updateLesson(command);

    // Assert - nullを渡した場合は元の値が保持される
    assertNotNull(result);
    assertEquals("タイトルのみ更新", result.getTitle());
    assertEquals("元の説明", result.getDescription()); // 元の値が保持される
    assertEquals("https://example.com/old-video.mp4", result.getVideoUrl()); // 元の値が保持される
  }

  @Test
  void 異常系_存在しないレッスンを更新すると例外が発生すること() {
    // Arrange
    LessonUpdateCommand command = LessonUpdateCommand.create(
        999, "存在しないレッスン", "説明", "https://example.com/video.mp4");

    // Act & Assert
    RuntimeException exception = assertThrows(
        RuntimeException.class,
        () -> lessonApplicationService.updateLesson(command)
    );
    assertEquals("Lesson not found with ID: 999", exception.getMessage());
  }

  @Test
  void 正常系_存在するレッスンを削除できること() {
    // Arrange - 削除対象のレッスンを準備（IDは自動生成）
    jdbcTemplate.update(
        "INSERT INTO courses (course_order, title, description, created_at, updated_at) VALUES (?, ?, ?, ?, ?)",
        new BigDecimal("1"), "テストコース", "コース説明", LocalDateTime.now(), LocalDateTime.now());
    Integer courseId = jdbcTemplate.queryForObject("SELECT id FROM courses WHERE title = ?", Integer.class, "テストコース");
    
    jdbcTemplate.update(
        "INSERT INTO lesson_groups (course_id, lesson_group_order, title, created_at, updated_at) VALUES (?, ?, ?, ?, ?)",
        courseId, new BigDecimal("1"), "テストグループ", LocalDateTime.now(), LocalDateTime.now());
    Integer lessonGroupId = jdbcTemplate.queryForObject("SELECT id FROM lesson_groups WHERE title = ?", Integer.class, "テストグループ");
    
    jdbcTemplate.update(
        "INSERT INTO lessons (lesson_group_id, course_id, lesson_order, title, description, video_url, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
        lessonGroupId, courseId, new BigDecimal("1"), "削除対象レッスン", "説明", "https://example.com/video.mp4", 
        LocalDateTime.now(), LocalDateTime.now());

    // 生成されたIDを取得
    Integer lessonId = jdbcTemplate.queryForObject(
        "SELECT id FROM lessons WHERE title = ?", Integer.class, "削除対象レッスン");

    // Act
    lessonApplicationService.deleteLessonById(lessonId);

    // Assert - レッスンが削除されていることを確認
    Integer count = jdbcTemplate.queryForObject(
        "SELECT COUNT(*) FROM lessons WHERE id = ?", Integer.class, lessonId);
    assertEquals(0, count);
  }

  @Test
  void 正常系_存在しないレッスンを削除してもエラーにならないこと() {
    // Act - 存在しないIDで削除を実行
    lessonApplicationService.deleteLessonById(999);

    // Assert - 例外が発生しないことを確認（このテストが成功すればOK）
  }

  @Test
  void 正常系_複数回の削除操作が安全に実行できること() {
    // Arrange - 削除対象のレッスンを準備（IDは自動生成）
    jdbcTemplate.update(
        "INSERT INTO courses (course_order, title, description, created_at, updated_at) VALUES (?, ?, ?, ?, ?)",
        new BigDecimal("1"), "テストコース", "コース説明", LocalDateTime.now(), LocalDateTime.now());
    Integer courseId = jdbcTemplate.queryForObject("SELECT id FROM courses WHERE title = ?", Integer.class, "テストコース");
    
    jdbcTemplate.update(
        "INSERT INTO lesson_groups (course_id, lesson_group_order, title, created_at, updated_at) VALUES (?, ?, ?, ?, ?)",
        courseId, new BigDecimal("1"), "テストグループ", LocalDateTime.now(), LocalDateTime.now());
    Integer lessonGroupId = jdbcTemplate.queryForObject("SELECT id FROM lesson_groups WHERE title = ?", Integer.class, "テストグループ");
    
    jdbcTemplate.update(
        "INSERT INTO lessons (lesson_group_id, course_id, lesson_order, title, description, video_url, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
        lessonGroupId, courseId, new BigDecimal("1"), "複数回削除テスト", "説明", "https://example.com/video.mp4", 
        LocalDateTime.now(), LocalDateTime.now());

    // 生成されたIDを取得
    Integer lessonId = jdbcTemplate.queryForObject(
        "SELECT id FROM lessons WHERE title = ?", Integer.class, "複数回削除テスト");

    // Act
    lessonApplicationService.deleteLessonById(lessonId);
    lessonApplicationService.deleteLessonById(lessonId); // 2回目

    // Assert - 2回目の削除もエラーにならないことを確認
    Integer count = jdbcTemplate.queryForObject(
        "SELECT COUNT(*) FROM lessons WHERE id = ?", Integer.class, lessonId);
    assertEquals(0, count);
  }
}
