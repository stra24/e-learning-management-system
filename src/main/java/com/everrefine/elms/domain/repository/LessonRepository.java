package com.everrefine.elms.domain.repository;

import com.everrefine.elms.application.command.LessonSearchCommand;
import com.everrefine.elms.application.dto.CourseLessonsDto;
import com.everrefine.elms.domain.model.lesson.Lesson;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LessonRepository {

  Optional<Lesson> findFirstLessonByCourseId(UUID courseId);

  Optional<Lesson> findById(UUID lessonId);

  List<Lesson> findLessons(LessonSearchCommand lessonSearchCommand);

  int countLessons(LessonSearchCommand lessonSearchCommand);
  
  CourseLessonsDto findLessonsGroupedByLessonGroup(UUID courseId);
}
