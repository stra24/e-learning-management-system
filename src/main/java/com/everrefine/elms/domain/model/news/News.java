package com.everrefine.elms.domain.model.news;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * お知らせのエンティティ。
 */
@Getter
@AllArgsConstructor
@Table("news")
public class News {

  @Id
  private final Integer id;
  @NotNull
  private Title title;
  @NotNull
  private Content content;
  @NotNull
  @Column("created_at")
  private LocalDateTime createdAt;
  @NotNull
  @Column("updated_at")
  private LocalDateTime updatedAt;

  /**
   * 新規作成用のお知らせを作成する。
   *
   * @param title   タイトル
   * @param content 内容
   * @return 新規作成用のお知らせ
   */
  public static News create(String title, String content) {
    LocalDateTime now = LocalDateTime.now();
    return new News(
        null,
        new Title(title),
        new Content(content),
        now,
        now
    );
  }

  /**
   * 更新用のお知らせを作成する。
   *
   * @param title   タイトル
   * @param content 内容
   * @return 更新用のお知らせ
   */
  public News update(String title, String content) {
    return new News(
        this.id,
        title == null ? this.title : new Title(title),
        content == null ? this.content : new Content(content),
        this.createdAt,
        LocalDateTime.now()
    );
  }
}