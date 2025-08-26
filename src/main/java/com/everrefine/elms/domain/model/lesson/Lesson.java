package com.everrefine.elms.domain.model.lesson;

import com.everrefine.elms.domain.model.Order;
import com.everrefine.elms.domain.model.Url;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * レッスンのエンティティ。
 */
@Getter
@AllArgsConstructor
@Table("lessons")
public class Lesson {

  @Id
  private final UUID id;
  @NotNull
  @Column("lesson_group_id")
  private UUID lessonGroupId;
  @NotNull
  @Column("course_id")
  private UUID courseId;
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
}
