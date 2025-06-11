package com.everrefine.elms.infrastructure.mapper;

import com.everrefine.elms.domain.model.course.Course;
import com.everrefine.elms.domain.model.course.CourseForUpdateRequest;
import com.everrefine.elms.domain.model.PagerForRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CourseMapper {

  int updateCourse(CourseForUpdateRequest course);

  int createCourse(Course course);

  int deleteCourseById(UUID id);

  Optional<Course> findCourseById(UUID id);

  List<Course> findCourses(PagerForRequest pagerForRequest);

  int countCourses();
}
