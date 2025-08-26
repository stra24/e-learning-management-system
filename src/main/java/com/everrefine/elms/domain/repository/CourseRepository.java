package com.everrefine.elms.domain.repository;

import com.everrefine.elms.domain.model.course.Course;
import com.everrefine.elms.domain.model.course.CourseForCreateRequest;
import com.everrefine.elms.domain.model.course.CourseForUpdateRequest;
import com.everrefine.elms.domain.model.PagerForRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseRepository {

  int updateCourse(CourseForUpdateRequest course);

  int createCourse(CourseForCreateRequest course);

  int deleteCourseById(UUID id);

  Optional<Course> findCourseById(UUID id);

  List<Course> findCourses(PagerForRequest pagerForRequest);

  int countCourses();
}
