package com.everrefine.elms.domain.model.lesson;

import com.everrefine.elms.domain.model.Order;
import com.everrefine.elms.domain.model.Url;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.Nullable;

/**
 * レッスンのエンティティ。
 */
@Getter
@AllArgsConstructor
@Table("lessons")
public class Lesson {

  @Id
  private final Integer id;
  @NotNull
  @Column("lesson_group_id")
  private Integer lessonGroupId;
  @NotNull
  @Column("course_id")
  private Integer courseId;
  @NotNull
  @Column("lesson_order")
  private Order lessonOrder;
  @NotNull
  private Title title;
  @Nullable
  private Description description;
  @Nullable
  @Column("video_url")
  private Url videoUrl;
  @NotNull
  @Column("created_at")
  private LocalDateTime createdAt;
  @NotNull
  @Column("updated_at")
  private LocalDateTime updatedAt;

  /**
   * 新規作成用のレッスンを作成する。
   *
   * @param lessonGroupId レッスングループID
   * @param courseId      コースID
   * @param lessonOrder   レッスンの並び順
   * @param title         レッスンタイトル
   * @param description   レッスンの説明
   * @param videoUrl      レッスンの動画URL
   * @return 新規作成用のレッスン
   */
  public static Lesson create(
      Integer lessonGroupId,
      Integer courseId,
      BigDecimal lessonOrder,
      String title,
      String description,
      String videoUrl
  ) {
    return new Lesson(
        null,
        lessonGroupId,
        courseId,
        new Order(lessonOrder),
        new Title(title),
        description == null ? null : new Description(description),
        videoUrl == null ? null : new Url(videoUrl),
        LocalDateTime.now(),
        LocalDateTime.now()
    );
  }

  /**
   * 更新用のレッスンを作成する。
   *
   * @param title       レッスンタイトル
   * @param description レッスンの説明
   * @param videoUrl    レッスンの動画URL
   * @return 更新用のレッスン
   */
  public Lesson update(String title, String description, String videoUrl) {
    return new Lesson(
        this.id,
        this.lessonGroupId,
        this.courseId,
        this.lessonOrder,
        title == null ? this.title : new Title(title),
        description == null ? this.description : new Description(description),
        videoUrl == null ? this.videoUrl : new Url(videoUrl),
        this.createdAt,
        LocalDateTime.now()
    );
  }
}
