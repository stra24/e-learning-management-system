package com.everrefine.elms.application.service;

import com.everrefine.elms.application.command.CourseCreateCommand;
import com.everrefine.elms.application.command.CourseUpdateCommand;
import com.everrefine.elms.application.dto.CourseDto;
import com.everrefine.elms.application.dto.CoursePageDto;

public interface CourseApplicationService {

  void updateCourse(CourseUpdateCommand courseUpdateCommand);

  void createCourse(CourseCreateCommand courseCreateCommand);

  void deleteCourseById(String courseId);

  CourseDto findCourseById(String CourseId);

  CoursePageDto findCourses(int pageNum, int pageSize);
}
