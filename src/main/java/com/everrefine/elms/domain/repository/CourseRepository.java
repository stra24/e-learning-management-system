package com.everrefine.elms.domain.repository;

import com.everrefine.elms.domain.model.Order;
import com.everrefine.elms.domain.model.course.Course;
import com.everrefine.elms.domain.model.course.CourseForCreateRequest;
import com.everrefine.elms.domain.model.course.CourseForUpdateRequest;
import com.everrefine.elms.domain.model.PagerForRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseRepository {

  void updateCourse(CourseForUpdateRequest course);

  void createCourse(CourseForCreateRequest course);

  void deleteCourseById(UUID id);

  Optional<Course> findCourseById(UUID id);

  List<Course> findCourses(PagerForRequest pagerForRequest);

  int countCourses();

  Order issueCourseOrder();
}
