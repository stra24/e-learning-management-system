package com.everrefine.elms.application.service;

import com.everrefine.elms.application.dto.FirstLessonDto;
import com.everrefine.elms.application.dto.LessonDto;

public interface LessonApplicationService {

  FirstLessonDto findFirstLessonIdByCourseId(String courseId);

  LessonDto findLessonById(String courseId, String lessonId);
}