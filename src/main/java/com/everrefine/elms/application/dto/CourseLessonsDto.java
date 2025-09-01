package com.everrefine.elms.application.dto;

import java.util.List;
import java.util.UUID;

public record CourseLessonsDto(
    UUID courseId,
    List<LessonGroupDto> lessonGroups
) {
}
