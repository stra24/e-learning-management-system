package com.everrefine.elms.domain.repository;

import com.everrefine.elms.application.command.LessonCreateCommand;
import com.everrefine.elms.application.command.LessonSearchCommand;
import com.everrefine.elms.application.dto.CourseLessonsDto;
import com.everrefine.elms.domain.model.lesson.Lesson;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface LessonRepository {

  Optional<Lesson> findFirstLessonByCourseId(UUID courseId);

  Optional<Lesson> findById(UUID lessonId);

  java.util.List<Lesson> findLessons(LessonSearchCommand lessonSearchCommand);

  int countLessons(LessonSearchCommand lessonSearchCommand);
  
  CourseLessonsDto findLessonsGroupedByLessonGroup(UUID courseId);
  
  Lesson createLesson(LessonCreateCommand lessonCreateCommand);
  
  Optional<BigDecimal> findMaxLessonOrderByLessonGroupId(UUID lessonGroupId);
}
