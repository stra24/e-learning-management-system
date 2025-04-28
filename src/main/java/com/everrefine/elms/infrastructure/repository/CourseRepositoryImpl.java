package com.everrefine.elms.infrastructure.repository;

import com.everrefine.elms.domain.model.course.Course;
import com.everrefine.elms.domain.model.pager.PagerForRequest;
import com.everrefine.elms.domain.model.course.Course;
import com.everrefine.elms.domain.model.course.CourseForUpdateRequest;
import com.everrefine.elms.domain.repository.CourseRepository;
import com.everrefine.elms.infrastructure.mapper.CourseMapper;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class CourseRepositoryImpl implements CourseRepository {

  private final CourseMapper courseMapper;

  @Override
  public int updateCourse(CourseForUpdateRequest course) {
    return courseMapper.updateCourse(course);
  }

  @Override
  public int createCourse(Course course) {
    return courseMapper.createCourse(course);
  }

  @Override
  public int deleteCourseById(UUID id) {
    return courseMapper.deleteCourseById(id);
  }
  
  @Override
  public Optional<Course> findCourseById(UUID id) {
    return courseMapper.findCourseById(id);
  }

  @Override
  public List<Course> findCourses(PagerForRequest pagerForRequest) {
    return courseMapper.findCourses(pagerForRequest);
  }

  @Override
  public int countCourses() {
    return courseMapper.countCourses();
  }
}