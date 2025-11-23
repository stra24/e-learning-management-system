package com.everrefine.elms.domain.model.lesson;

import com.everrefine.elms.domain.model.Order;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * レッスングループのエンティティ。
 */
@Getter
@AllArgsConstructor
@Table("lesson_groups")
public class LessonGroup {

  @Id
  private final Integer id;

  @NotNull
  @Column("course_id")
  private Integer courseId;

  @NotNull
  @Column("lesson_group_order")
  private Order lessonGroupOrder;

  @NotNull
  private Title title;

  @NotNull
  @Column("created_at")
  private LocalDateTime createdAt;

  @NotNull
  @Column("updated_at")
  private LocalDateTime updatedAt;

  // レッスンリスト（JOINで取得する場合に使用）
  private List<Lesson> lessons;

  /**
   * 新規作成用のレッスングループを作成する。
   *
   * @param courseId         コースID
   * @param lessonGroupOrder レッスングループの並び順
   * @param title            レッスングループタイトル
   * @return 新規作成用のレッスングループ
   */
  public static LessonGroup create(
      Integer courseId,
      BigDecimal lessonGroupOrder,
      String title
  ) {
    return new LessonGroup(
        null,
        courseId,
        new Order(lessonGroupOrder),
        new Title(title),
        LocalDateTime.now(),
        LocalDateTime.now(),
        null
    );
  }
}
