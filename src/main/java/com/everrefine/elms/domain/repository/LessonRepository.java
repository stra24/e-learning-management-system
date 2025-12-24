package com.everrefine.elms.domain.repository;

import com.everrefine.elms.application.command.LessonSearchCommand;
import com.everrefine.elms.application.dto.CourseLessonsDto;
import com.everrefine.elms.domain.model.lesson.Lesson;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface LessonRepository {

  Optional<Lesson> findFirstLessonByCourseId(Integer courseId);

  Optional<Lesson> findById(Integer lessonId);

  List<Lesson> findByIdIn(List<Integer> lessonIds);

  java.util.List<Lesson> findLessons(LessonSearchCommand lessonSearchCommand);

  int countLessons(LessonSearchCommand lessonSearchCommand);

  List<Lesson> findLessonsByLessonGroupId(Integer lessonGroupId);

  CourseLessonsDto findLessonsGroupedByLessonGroup(Integer courseId);

  Lesson createLesson(Lesson lesson);

  Lesson updateLesson(Lesson lesson);

  Optional<BigDecimal> findMaxLessonOrderByLessonGroupId(Integer lessonGroupId);

  void deleteLessonById(Integer lessonId);
}
