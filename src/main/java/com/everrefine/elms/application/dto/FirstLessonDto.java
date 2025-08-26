package com.everrefine.elms.application.dto;

import java.util.UUID;

public record FirstLessonDto(
    boolean existsLesson,
    UUID firstLessonId
) {

}