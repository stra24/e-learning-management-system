package com.everrefine.elms.application.service;

import com.everrefine.elms.application.command.LessonGroupCreateCommand;
import com.everrefine.elms.application.command.LessonGroupUpdateCommand;
import com.everrefine.elms.application.dto.LessonGroupDto;

public interface LessonGroupApplicationService {

  LessonGroupDto createLessonGroup(LessonGroupCreateCommand lessonGroupCreateCommand);

  LessonGroupDto updateLessonGroup(LessonGroupUpdateCommand lessonGroupUpdateCommand, Integer courseId);

  void deleteLessonGroupById(Integer lessonGroupId);
}
