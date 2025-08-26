package com.everrefine.elms.infrastructure.mapper;

import com.everrefine.elms.domain.model.PagerForRequest;
import com.everrefine.elms.domain.model.course.Course;
import com.everrefine.elms.domain.model.course.CourseForCreateRequest;
import com.everrefine.elms.domain.model.course.CourseForUpdateRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CourseMapper {

  int updateCourse(CourseForUpdateRequest course);

  int createCourse(CourseForCreateRequest course);

  int deleteCourseById(UUID id);

  Optional<Course> findCourseById(UUID id);

  List<Course> findCourses(PagerForRequest pagerForRequest);

  int countCourses();
}
