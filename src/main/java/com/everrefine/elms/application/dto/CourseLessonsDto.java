package com.everrefine.elms.application.dto;

import java.util.List;

public record CourseLessonsDto(
    Integer courseId,
    List<LessonGroupDto> lessonGroups
) {
}
