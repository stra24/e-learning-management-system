package com.everrefine.elms.application.service;

import com.everrefine.elms.application.command.LessonCreateCommand;
import com.everrefine.elms.application.command.LessonSearchCommand;
import com.everrefine.elms.application.command.LessonUpdateCommand;
import com.everrefine.elms.application.dto.CourseLessonsDto;
import com.everrefine.elms.application.dto.FirstLessonDto;
import com.everrefine.elms.application.dto.LessonDto;
import com.everrefine.elms.application.dto.LessonPageDto;

public interface LessonApplicationService {

  FirstLessonDto findFirstLessonIdByCourseId(Integer courseId);

  LessonDto findLessonById(Integer courseId, Integer lessonId);

  LessonPageDto findLessons(LessonSearchCommand lessonSearchCommand);

  CourseLessonsDto findLessonsGroupedByLessonGroup(Integer courseId);

  LessonDto createLesson(LessonCreateCommand lessonCreateCommand);

  LessonDto updateLesson(LessonUpdateCommand lessonUpdateCommand);
 
  void deleteLessonById(Integer lessonId);

  LessonDto updateLessonOrder(Integer operatorUserId, Integer lessonId, Integer precedingLessonId, Integer followingLessonId);
}
