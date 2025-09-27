package com.everrefine.elms.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.everrefine.elms.application.command.NewsCreateCommand;
import com.everrefine.elms.application.dto.NewsDto;
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
}
