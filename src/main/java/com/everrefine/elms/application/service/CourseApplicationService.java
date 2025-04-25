package com.everrefine.elms.application.service;

import com.everrefine.elms.application.dto.CourseDto;
import com.everrefine.elms.application.dto.CoursePageDto;

public interface CourseApplicationService {

  CourseDto findCourseById(String CourseId);

  CoursePageDto findCourses(int pageNum, int pageSize);
}
