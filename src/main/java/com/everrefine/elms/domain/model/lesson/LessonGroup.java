package com.everrefine.elms.domain.model.lesson;

import com.everrefine.elms.domain.model.Order;
import jakarta.validation.constraints.NotNull;
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
  private Title name;
  
  @NotNull
  @Column("created_at")
  private LocalDateTime createdAt;
  
  @NotNull
  @Column("updated_at")
  private LocalDateTime updatedAt;
  
  // レッスンリスト（JOINで取得する場合に使用）
  private List<Lesson> lessons;
}
