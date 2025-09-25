package com.everrefine.elms.domain.repository;

import com.everrefine.elms.domain.model.Order;
import com.everrefine.elms.domain.model.course.Course;
import com.everrefine.elms.domain.model.PagerForRequest;
import java.util.List;
import java.util.Optional;

public interface CourseRepository {

  void updateCourse(Course course);

  void createCourse(Course course);

  void deleteCourseById(Integer id);

  Optional<Course> findCourseById(Integer id);

  List<Course> findCourses(PagerForRequest pagerForRequest);

  int countCourses();

  Order issueCourseOrder();
}
