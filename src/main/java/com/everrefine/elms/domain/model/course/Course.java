package com.everrefine.elms.domain.model.course;

import com.everrefine.elms.domain.model.Order;
import com.everrefine.elms.domain.model.Url;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * コースのエンティティ。
 */
@Getter
@AllArgsConstructor
@Table("courses")
public class Course {

  @Id
  private final UUID id;
  @Nullable
  @Column("thumbnail_url")
  private Url thumbnailUrl;
  @NotNull
  private Title title;
  @Nullable
  private Description description;
  @NotNull
  @Column("course_order")
  private Order courseOrder;
  @NotNull
  @Column("created_at")
  private LocalDateTime createdAt;
  @NotNull
  @Column("updated_at")
  private LocalDateTime updatedAt;
}