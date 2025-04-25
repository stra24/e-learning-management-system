package com.everrefine.elms.infrastructure.mapper;

import com.everrefine.elms.domain.model.course.Course;
import com.everrefine.elms.domain.model.pager.PagerRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CourseMapper {

  Optional<Course> findCourseById(UUID id);

  List<Course> findCourses(PagerRequest pagerRequest);

  int countCourses();
}
