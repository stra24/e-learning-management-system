package com.everrefine.elms.application.service;

import com.everrefine.elms.application.command.LessonGroupCreateCommand;
import com.everrefine.elms.application.dto.LessonGroupDto;

public interface LessonGroupApplicationService {

  LessonGroupDto createLessonGroup(LessonGroupCreateCommand lessonGroupCreateCommand);

  void deleteLessonGroupById(Integer lessonGroupId);
}
