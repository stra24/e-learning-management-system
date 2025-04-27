package com.everrefine.elms.domain.repository;

import com.everrefine.elms.domain.model.course.Course;
import com.everrefine.elms.domain.model.pager.PagerForRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseRepository {

  Optional<Course> findCourseById(UUID id);

  List<Course> findCourses(PagerForRequest pagerForRequest);

  int countCourses();
}
