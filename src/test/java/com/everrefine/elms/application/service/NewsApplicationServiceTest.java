package com.everrefine.elms.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.everrefine.elms.application.command.NewsCreateCommand;
import com.everrefine.elms.application.command.NewsSearchCommand;
import com.everrefine.elms.application.dto.NewsDto;
import com.everrefine.elms.application.dto.NewsPageDto;
import java.sql.Timestamp;
import java.time.LocalDate;
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

@SpringBootTest(webEnvironment = WebEnvironment.NONE) // WebまわりのConfigurationはBean生成を無効にして高速化する。
@Testcontainers // DBはDockerコンテナを使用する。
@Transactional // 各テストメソッド終了時にテストデータをロールバックする。
public class NewsApplicationServiceTest {

  /**
   * テストで使うDBを用意する。
   */
  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgres =
      new PostgreSQLContainer<>("postgres:17").withReuse(true);

  /**
   * テスト対象のサービスクラス。
   */
  @Autowired
  private NewsApplicationServiceImpl newsApplicationService;

  /**
   * データ検証で使用するためのJdbcTemplate。
   */
  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Test
  void 正常系_お知らせを新規作成できること() {
    // Arrange
    newsApplicationService.createNews(NewsCreateCommand.create("テストタイトル", "テスト本文"));

    // 直近で作成されたIDを取得する。
    Integer id = jdbcTemplate.queryForObject(
        """
            select max(id)
            from news
            """,
        Integer.class
    );
    assertNotNull(id);

    // Act
    NewsDto dto = newsApplicationService.findNewsById(id);

    // Assert
    assertEquals(id, dto.getId());
    assertEquals("テストタイトル", dto.getTitle());
    assertEquals("テスト本文", dto.getContent());
  }

  @Test
  void 正常系_タイトル部分一致と日付範囲_ページング降順で取得できること() {
    // Arrange
    // データ5件投入（作成日をバラして後で日付範囲・並び順テストに使う）
    insertNewsWithDate("Java入門", "A", LocalDate.of(2025, 1, 1));  // 範囲外（後で除外される）
    insertNewsWithDate("Spring解説", "B", LocalDate.of(2025, 2, 1)); // タイトル不一致（後で除外）
    insertNewsWithDate("Java実践", "C", LocalDate.of(2025, 3, 1));   // 範囲内・一致
    insertNewsWithDate("旅行記", "D", LocalDate.of(2025, 4, 1));     // タイトル不一致（後で除外）
    insertNewsWithDate("Javaニュース", "E", LocalDate.of(2025, 5, 1)); // 範囲内・一致（最新）

    // Act:
    // タイトルに「Java」を含み、2025-02-01〜2025-05-31（両端含む*）の1ページ目・2件取得をリクエストする。
    var cmd = NewsSearchCommand.create(
        1,
        2,
        "Java",
        LocalDate.of(2025, 2, 1),
        LocalDate.of(2025, 5, 31)
    );
    NewsPageDto page = newsApplicationService.findNews(cmd);

    // Assert
    assertEquals(2, page.getTotalSize());
    assertEquals(1, page.getPageNum());
    assertEquals(2, page.getPageSize());

    var items = page.getNewsDtos();
    assertEquals(2, items.size());
    assertEquals("Javaニュース", items.get(0).getTitle());
    assertEquals("Java実践", items.get(1).getTitle());
  }

  /**
   * Service経由で作成→直後に created_at / updated_at を指定日に上書きして並び順を固定する。
   */
  private void insertNewsWithDate(String title, String content, LocalDate createdDate) {
    newsApplicationService.createNews(NewsCreateCommand.create(title, content));

    // 直近で採番されたIDを取得（単体テスト内での連続実行を前提）
    Integer id = jdbcTemplate.queryForObject(
        "SELECT id FROM news ORDER BY id DESC LIMIT 1",
        Integer.class
    );

    // created_at / updated_at をテスト都合の日付に調整
    LocalDateTime at = createdDate.atTime(10, 0, 0);
    Timestamp ts = Timestamp.valueOf(at);
    jdbcTemplate.update(
        "UPDATE news SET created_at = ?, updated_at = ? WHERE id = ?",
        ps -> {
          ps.setTimestamp(1, ts);
          ps.setTimestamp(2, ts);
          ps.setInt(3, id);
        }
    );
  }

  @Test
  void 正常系_お知らせを更新できること() {
    // Arrange: まず1件作成
    newsApplicationService.createNews(NewsCreateCommand.create("初期タイトル", "初期本文"));
    Integer id = jdbcTemplate.queryForObject(
        "SELECT id FROM news ORDER BY id DESC LIMIT 1",
        Integer.class
    );
    assertNotNull(id);

    // （任意）更新前の updated_at を保持して後で更新されたことも検証
    Timestamp beforeUpdatedAt = jdbcTemplate.queryForObject(
        "SELECT updated_at FROM news WHERE id = ?",
        Timestamp.class,
        id
    );

    // Act: タイトルと本文を更新
    newsApplicationService.updateNews(
        com.everrefine.elms.application.command.NewsUpdateCommand.create(
            id, "更新後タイトル", "更新後本文"
        )
    );

    // Assert: DTO経由で内容が更新されていること
    NewsDto dto = newsApplicationService.findNewsById(id);
    assertEquals(id, dto.getId());
    assertEquals("更新後タイトル", dto.getTitle());
    assertEquals("更新後本文", dto.getContent());

    // DB上も行数が1件のままであること（上書き更新）
    Integer cnt = jdbcTemplate.queryForObject(
        "SELECT COUNT(*) FROM news WHERE id = ?",
        Integer.class,
        id
    );
    assertEquals(1, cnt);

    // （任意）updated_at が更新されていること
    Timestamp afterUpdatedAt = jdbcTemplate.queryForObject(
        "SELECT updated_at FROM news WHERE id = ?",
        Timestamp.class,
        id
    );
    assertNotNull(beforeUpdatedAt);
    assertNotNull(afterUpdatedAt);
    assertTrue(afterUpdatedAt.after(beforeUpdatedAt),
        () -> "updated_at が更新前と同一か過去になっています: before=" + beforeUpdatedAt + ", after=" + afterUpdatedAt);
  }

  @Test
  void 異常系_存在しないIDを更新すると例外になること() {
    // Arrange
    int notExistsId = -9999;

    // Act & Assert
    assertThrows(RuntimeException.class, () ->
        newsApplicationService.updateNews(
            com.everrefine.elms.application.command.NewsUpdateCommand.create(
                notExistsId, "何か", "何か"
            )
        )
    );
  }
}
