package com.everrefine.elms.application.service;

import com.everrefine.elms.application.dto.FirstLessonDto;

public interface LessonApplicationService {

  FirstLessonDto findFirstLessonIdByCourseId(String courseId);
}