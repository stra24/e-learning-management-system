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
    assertEquals(new BigDecimal("1.0000000000"), result.getLessonOrder());
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
  void 正常系_精度枯渇が疑われる場合に再採番後に並び替えできること() {
    jdbcTemplate.update(
        "INSERT INTO courses (course_order, title, description, created_at, updated_at) VALUES (?, ?, ?, ?, ?)",
        new BigDecimal("1"), "精度枯渇テストコース", "コース説明", LocalDateTime.now(), LocalDateTime.now());
    Integer courseId = jdbcTemplate.queryForObject("SELECT id FROM courses WHERE title = ?", Integer.class,
        "精度枯渇テストコース");

    jdbcTemplate.update(
        "INSERT INTO lesson_groups (course_id, lesson_group_order, title, created_at, updated_at) VALUES (?, ?, ?, ?, ?)",
        courseId, new BigDecimal("1"), "精度枯渇テストグループ", LocalDateTime.now(), LocalDateTime.now());
    Integer lessonGroupId = jdbcTemplate.queryForObject("SELECT id FROM lesson_groups WHERE title = ?", Integer.class,
        "精度枯渇テストグループ");

    jdbcTemplate.update(
        "INSERT INTO lessons (lesson_group_id, course_id, lesson_order, title, description, video_url, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
        lessonGroupId, courseId, new BigDecimal("0.0000000001"), "精度レッスン1", "説明", "https://example.com/v1.mp4",
        LocalDateTime.now(), LocalDateTime.now());
    Integer lesson1Id = jdbcTemplate.queryForObject("SELECT id FROM lessons WHERE title = ?", Integer.class, "精度レッスン1");

    jdbcTemplate.update(
        "INSERT INTO lessons (lesson_group_id, course_id, lesson_order, title, description, video_url, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
        lessonGroupId, courseId, new BigDecimal("0.0000000002"), "精度レッスン2", "説明", "https://example.com/v2.mp4",
        LocalDateTime.now(), LocalDateTime.now());
    Integer lesson2Id = jdbcTemplate.queryForObject("SELECT id FROM lessons WHERE title = ?", Integer.class, "精度レッスン2");

    jdbcTemplate.update(
        "INSERT INTO lessons (lesson_group_id, course_id, lesson_order, title, description, video_url, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
        lessonGroupId, courseId, new BigDecimal("0.0000000003"), "精度レッスン3", "説明", "https://example.com/v3.mp4",
        LocalDateTime.now(), LocalDateTime.now());
    Integer lesson3Id = jdbcTemplate.queryForObject("SELECT id FROM lessons WHERE title = ?", Integer.class, "精度レッスン3");

    LessonDto result = lessonApplicationService.updateLessonOrder(1, lesson3Id, lesson1Id, lesson2Id);

    BigDecimal lesson1Order = jdbcTemplate.queryForObject(
        "SELECT lesson_order FROM lessons WHERE id = ?", BigDecimal.class, lesson1Id);
    BigDecimal lesson2Order = jdbcTemplate.queryForObject(
        "SELECT lesson_order FROM lessons WHERE id = ?", BigDecimal.class, lesson2Id);

    assertEquals(new BigDecimal("1024.0000000000"), lesson1Order);
    assertEquals(new BigDecimal("2048.0000000000"), lesson2Order);
    assertEquals(new BigDecimal("1536.0000000000"), result.getLessonOrder());
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

  @Test
  void 正常系_2つのレッスンの間に移動できること() {
    // Arrange - テストデータを準備
    jdbcTemplate.update(
        "INSERT INTO courses (course_order, title, description, created_at, updated_at) VALUES (?, ?, ?, ?, ?)",
        new BigDecimal("1"), "テストコース", "コース説明", LocalDateTime.now(), LocalDateTime.now());
    Integer courseId = jdbcTemplate.queryForObject("SELECT id FROM courses WHERE title = ?", Integer.class, "テストコース");
    
    jdbcTemplate.update(
        "INSERT INTO lesson_groups (course_id, lesson_group_order, title, created_at, updated_at) VALUES (?, ?, ?, ?, ?)",
        courseId, new BigDecimal("1"), "テストグループ", LocalDateTime.now(), LocalDateTime.now());
    Integer lessonGroupId = jdbcTemplate.queryForObject("SELECT id FROM lesson_groups WHERE title = ?", Integer.class, "テストグループ");
    
    // 3つのレッスンを作成（order: 1000, 2000, 3000）
    jdbcTemplate.update(
        "INSERT INTO lessons (lesson_group_id, course_id, lesson_order, title, description, video_url, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
        lessonGroupId, courseId, new BigDecimal("1000"), "レッスン1", "説明1", "https://example.com/video1.mp4", 
        LocalDateTime.now(), LocalDateTime.now());
    Integer lesson1Id = jdbcTemplate.queryForObject("SELECT id FROM lessons WHERE title = ?", Integer.class, "レッスン1");
    
    jdbcTemplate.update(
        "INSERT INTO lessons (lesson_group_id, course_id, lesson_order, title, description, video_url, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
        lessonGroupId, courseId, new BigDecimal("2000"), "レッスン2", "説明2", "https://example.com/video2.mp4", 
        LocalDateTime.now(), LocalDateTime.now());
    Integer lesson2Id = jdbcTemplate.queryForObject("SELECT id FROM lessons WHERE title = ?", Integer.class, "レッスン2");
    
    jdbcTemplate.update(
        "INSERT INTO lessons (lesson_group_id, course_id, lesson_order, title, description, video_url, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
        lessonGroupId, courseId, new BigDecimal("3000"), "レッスン3", "説明3", "https://example.com/video3.mp4", 
        LocalDateTime.now(), LocalDateTime.now());
    Integer lesson3Id = jdbcTemplate.queryForObject("SELECT id FROM lessons WHERE title = ?", Integer.class, "レッスン3");

    // Act - レッスン3をレッスン1と2の間に移動
    LessonDto result = lessonApplicationService.updateLessonOrder(1, lesson3Id, lesson1Id, lesson2Id);

    // Assert - 新しい順序は (1000 + 2000) / 2 = 1500
    assertNotNull(result);
    assertEquals(lesson3Id, result.getId());
    assertEquals(new BigDecimal("1500.0000000000"), result.getLessonOrder());
    
    // DBが更新されていることを確認
    BigDecimal updatedOrder = jdbcTemplate.queryForObject(
        "SELECT lesson_order FROM lessons WHERE id = ?", BigDecimal.class, lesson3Id);
    assertEquals(new BigDecimal("1500.0000000000"), updatedOrder);
  }

  @Test
  void 正常系_先頭に移動できること() {
    // Arrange - テストデータを準備
    jdbcTemplate.update(
        "INSERT INTO courses (course_order, title, description, created_at, updated_at) VALUES (?, ?, ?, ?, ?)",
        new BigDecimal("1"), "テストコース", "コース説明", LocalDateTime.now(), LocalDateTime.now());
    Integer courseId = jdbcTemplate.queryForObject("SELECT id FROM courses WHERE title = ?", Integer.class, "テストコース");
    
    jdbcTemplate.update(
        "INSERT INTO lesson_groups (course_id, lesson_group_order, title, created_at, updated_at) VALUES (?, ?, ?, ?, ?)",
        courseId, new BigDecimal("1"), "テストグループ", LocalDateTime.now(), LocalDateTime.now());
    Integer lessonGroupId = jdbcTemplate.queryForObject("SELECT id FROM lesson_groups WHERE title = ?", Integer.class, "テストグループ");
    
    // 2つのレッスンを作成
    jdbcTemplate.update(
        "INSERT INTO lessons (lesson_group_id, course_id, lesson_order, title, description, video_url, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
        lessonGroupId, courseId, new BigDecimal("1000"), "レッスン1", "説明1", "https://example.com/video1.mp4", 
        LocalDateTime.now(), LocalDateTime.now());
    Integer lesson1Id = jdbcTemplate.queryForObject("SELECT id FROM lessons WHERE title = ?", Integer.class, "レッスン1");
    
    jdbcTemplate.update(
        "INSERT INTO lessons (lesson_group_id, course_id, lesson_order, title, description, video_url, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
        lessonGroupId, courseId, new BigDecimal("2000"), "レッスン2", "説明2", "https://example.com/video2.mp4", 
        LocalDateTime.now(), LocalDateTime.now());
    Integer lesson2Id = jdbcTemplate.queryForObject("SELECT id FROM lessons WHERE title = ?", Integer.class, "レッスン2");

    // Act - レッスン2を先頭に移動（precedingLessonIdをnullに）
    LessonDto result = lessonApplicationService.updateLessonOrder(1, lesson2Id, null, lesson1Id);

    // Assert - 新しい順序は 1000 / 2 = 500
    assertNotNull(result);
    assertEquals(lesson2Id, result.getId());
    assertEquals(new BigDecimal("500.0000000000"), result.getLessonOrder());
    
    // DBが更新されていることを確認
    BigDecimal updatedOrder = jdbcTemplate.queryForObject(
        "SELECT lesson_order FROM lessons WHERE id = ?", BigDecimal.class, lesson2Id);
    assertEquals(new BigDecimal("500.0000000000"), updatedOrder);
  }

  @Test
  void 正常系_末尾に移動できること() {
    // Arrange - テストデータを準備
    jdbcTemplate.update(
        "INSERT INTO courses (course_order, title, description, created_at, updated_at) VALUES (?, ?, ?, ?, ?)",
        new BigDecimal("1"), "テストコース", "コース説明", LocalDateTime.now(), LocalDateTime.now());
    Integer courseId = jdbcTemplate.queryForObject("SELECT id FROM courses WHERE title = ?", Integer.class, "テストコース");
    
    jdbcTemplate.update(
        "INSERT INTO lesson_groups (course_id, lesson_group_order, title, created_at, updated_at) VALUES (?, ?, ?, ?, ?)",
        courseId, new BigDecimal("1"), "テストグループ", LocalDateTime.now(), LocalDateTime.now());
    Integer lessonGroupId = jdbcTemplate.queryForObject("SELECT id FROM lesson_groups WHERE title = ?", Integer.class, "テストグループ");
    
    // 2つのレッスンを作成
    jdbcTemplate.update(
        "INSERT INTO lessons (lesson_group_id, course_id, lesson_order, title, description, video_url, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
        lessonGroupId, courseId, new BigDecimal("1000"), "レッスン1", "説明1", "https://example.com/video1.mp4", 
        LocalDateTime.now(), LocalDateTime.now());
    Integer lesson1Id = jdbcTemplate.queryForObject("SELECT id FROM lessons WHERE title = ?", Integer.class, "レッスン1");
    
    jdbcTemplate.update(
        "INSERT INTO lessons (lesson_group_id, course_id, lesson_order, title, description, video_url, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
        lessonGroupId, courseId, new BigDecimal("2000"), "レッスン2", "説明2", "https://example.com/video2.mp4", 
        LocalDateTime.now(), LocalDateTime.now());
    Integer lesson2Id = jdbcTemplate.queryForObject("SELECT id FROM lessons WHERE title = ?", Integer.class, "レッスン2");

    // Act - レッスン1を末尾に移動（followingLessonIdをnullに）
    LessonDto result = lessonApplicationService.updateLessonOrder(1, lesson1Id, lesson2Id, null);

    // Assert - 新しい順序は 2000 + 1024 = 3024
    assertNotNull(result);
    assertEquals(lesson1Id, result.getId());
    assertEquals(new BigDecimal("3024.0000000000"), result.getLessonOrder());
    
    // DBが更新されていることを確認
    BigDecimal updatedOrder = jdbcTemplate.queryForObject(
        "SELECT lesson_order FROM lessons WHERE id = ?", BigDecimal.class, lesson1Id);
    assertEquals(new BigDecimal("3024.0000000000"), updatedOrder);
  }

  @Test
  void 異常系_存在しないレッスンIDで並び替えすると例外が発生すること() {
    // Arrange - 存在するレッスンを1つ準備
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
        lessonGroupId, courseId, new BigDecimal("1000"), "レッスン1", "説明1", "https://example.com/video1.mp4", 
        LocalDateTime.now(), LocalDateTime.now());
    Integer lesson1Id = jdbcTemplate.queryForObject("SELECT id FROM lessons WHERE title = ?", Integer.class, "レッスン1");

    // Act & Assert - 存在しないレッスンIDで並び替え
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> lessonApplicationService.updateLessonOrder(1, 999, lesson1Id, null)
    );
    assertEquals("Lesson not found with ID: 999", exception.getMessage());
  }

  @Test
  void 異常系_存在しないprecedingLessonIdで例外が発生すること() {
    // Arrange - テストデータを準備
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
        lessonGroupId, courseId, new BigDecimal("1000"), "レッスン1", "説明1", "https://example.com/video1.mp4", 
        LocalDateTime.now(), LocalDateTime.now());
    Integer lesson1Id = jdbcTemplate.queryForObject("SELECT id FROM lessons WHERE title = ?", Integer.class, "レッスン1");

    // Act & Assert - 存在しないprecedingLessonId
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> lessonApplicationService.updateLessonOrder(1, lesson1Id, 999, null)
    );
    assertEquals("Preceding lesson not found with ID: 999", exception.getMessage());
  }

  @Test
  void 異常系_存在しないfollowingLessonIdで例外が発生すること() {
    // Arrange - テストデータを準備
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
        lessonGroupId, courseId, new BigDecimal("1000"), "レッスン1", "説明1", "https://example.com/video1.mp4", 
        LocalDateTime.now(), LocalDateTime.now());
    Integer lesson1Id = jdbcTemplate.queryForObject("SELECT id FROM lessons WHERE title = ?", Integer.class, "レッスン1");

    // Act & Assert - 存在しないfollowingLessonId
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> lessonApplicationService.updateLessonOrder(1, lesson1Id, null, 999)
    );
    assertEquals("Following lesson not found with ID: 999", exception.getMessage());
  }
}
