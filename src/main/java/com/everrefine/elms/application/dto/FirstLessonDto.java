package com.everrefine.elms.application.dto;


public record FirstLessonDto(
    boolean existsLesson,
    Integer firstLessonGroupId,
    Integer firstLessonId
) {

}