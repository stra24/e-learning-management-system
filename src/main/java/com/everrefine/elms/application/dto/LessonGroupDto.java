package com.everrefine.elms.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record LessonGroupDto(
    Integer id,
    Integer courseId,
    BigDecimal lessonGroupOrder,
    String name,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    List<LessonDto> lessons
) {
}
