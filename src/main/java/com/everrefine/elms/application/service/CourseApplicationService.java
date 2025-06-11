package com.everrefine.elms.application.service;

import com.everrefine.elms.application.command.CourseCreateCommand;
import com.everrefine.elms.application.command.CourseUpdateCommand;
import com.everrefine.elms.application.dto.CourseDto;
import com.everrefine.elms.application.dto.CoursePageDto;

public interface CourseApplicationService {

  CourseDto findCourseById(String CourseId);

  CoursePageDto findCourses(int pageNum, int pageSize);

  void createCourse(CourseCreateCommand courseCreateCommand);

  void updateCourse(CourseUpdateCommand courseUpdateCommand);

  void deleteCourseById(String courseId);
}
