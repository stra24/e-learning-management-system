package com.everrefine.elms.application.service;

import com.everrefine.elms.application.command.LessonSearchCommand;
import com.everrefine.elms.application.dto.CourseLessonsDto;
import com.everrefine.elms.application.dto.FirstLessonDto;
import com.everrefine.elms.application.dto.LessonDto;
import com.everrefine.elms.application.dto.LessonPageDto;

public interface LessonApplicationService {

  FirstLessonDto findFirstLessonIdByCourseId(String courseId);

  LessonDto findLessonById(String courseId, String lessonId);

  LessonPageDto findLessons(LessonSearchCommand lessonSearchCommand);
  
  CourseLessonsDto findLessonsGroupedByLessonGroup(String courseId);
}