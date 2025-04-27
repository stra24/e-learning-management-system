package com.everrefine.elms.infrastructure.repository;

import com.everrefine.elms.domain.model.course.Course;
import com.everrefine.elms.domain.model.pager.PagerForRequest;
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