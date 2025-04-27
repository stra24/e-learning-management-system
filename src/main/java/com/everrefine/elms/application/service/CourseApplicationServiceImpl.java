package com.everrefine.elms.application.service;

import com.everrefine.elms.application.dto.CourseDto;
import com.everrefine.elms.application.dto.CoursePageDto;
import com.everrefine.elms.domain.model.course.Course;
import com.everrefine.elms.domain.model.pager.PagerForRequest;
import com.everrefine.elms.domain.model.pager.PagerForResponse;
import com.everrefine.elms.domain.repository.CourseRepository;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CourseApplicationServiceImpl implements CourseApplicationService {

  private final CourseRepository courseRepository;

  @Override
  public CourseDto findCourseById(String courseId) {
    UUID uuid = UUID.fromString(courseId);
    Course course = courseRepository.findCourseById(uuid)
        .orElseThrow(() -> new RuntimeException("Course not found with ID: " + courseId));
    return new CourseDto(course);
  }

  @Override
  public CoursePageDto findCourses(int pageNum, int pageSize) {
    PagerForRequest pagerForRequest = new PagerForRequest(pageNum, pageSize);
    List<Course> courses = courseRepository.findCourses(pagerForRequest);
    int totalSize = courseRepository.countCourses();
    PagerForResponse pagerForResponse = new PagerForResponse(pageNum, pageSize, totalSize);
    return new CoursePageDto(courses, pagerForResponse);
  }
}