package com.everrefine.elms.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record LessonGroupDto(
    UUID id,
    UUID courseId,
    BigDecimal lessonGroupOrder,
    String name,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    List<LessonDto> lessons
) {
}
