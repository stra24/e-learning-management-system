package com.everrefine.elms.application.service;

import com.everrefine.elms.application.command.CourseCreateCommand;
import com.everrefine.elms.application.command.CourseUpdateCommand;
import com.everrefine.elms.application.dto.CourseDto;
import com.everrefine.elms.application.dto.CoursePageDto;
import com.everrefine.elms.application.dto.converter.CourseDtoConverter;
import com.everrefine.elms.domain.model.Order;
import com.everrefine.elms.domain.model.PagerForRequest;
import com.everrefine.elms.domain.model.Url;
import com.everrefine.elms.domain.model.course.Course;
import com.everrefine.elms.domain.model.course.Description;
import com.everrefine.elms.domain.model.course.Title;
import com.everrefine.elms.domain.repository.CourseRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CourseApplicationServiceImpl implements CourseApplicationService {

  private final CourseRepository courseRepository;

  @Override
  @Transactional(readOnly = true)
  public CourseDto findCourseById(Integer courseId) {
    Course course = courseRepository.findCourseById(courseId)
        .orElseThrow(() -> new RuntimeException("Course not found with ID: " + courseId));
    return CourseDtoConverter.toDto(course);
  }

  @Override
  @Transactional(readOnly = true)
  public CoursePageDto findCourses(int pageNum, int pageSize) {
    PagerForRequest pagerForRequest = new PagerForRequest(pageNum, pageSize);
    List<Course> courses = courseRepository.findCourses(pagerForRequest);
    int totalSize = courseRepository.countCourses();
    List<CourseDto> courseDtos = courses.stream()
        .map(CourseDtoConverter::toDto)
        .toList();
    return new CoursePageDto(courseDtos, pageNum, pageSize, totalSize);
  }

  @Override
  @Transactional
  public void createCourse(CourseCreateCommand courseCreateCommand) {
    Order courseOrder = courseRepository.issueCourseOrder();
    Course course = Course.create(
        courseCreateCommand.getThumbnailUrl(),
        courseCreateCommand.getTitle(),
        courseCreateCommand.getDescription(),
        courseOrder.getValue()
    );
    courseRepository.createCourse(course);
  }

  @Override
  @Transactional
  public void updateCourse(CourseUpdateCommand courseUpdateCommand) {
    Course course = courseRepository.findCourseById(courseUpdateCommand.getId())
        .orElseThrow(() -> new RuntimeException("Course not found with ID: " + courseUpdateCommand.getId()));
    Course updatedCourse = course.update(
        courseUpdateCommand.getThumbnailUrl(),
        courseUpdateCommand.getTitle(),
        courseUpdateCommand.getDescription(),
        courseUpdateCommand.getCourseOrder()
    );
    courseRepository.updateCourse(updatedCourse);
  }

  @Override
  @Transactional
  public void deleteCourseById(Integer courseId) {
    // コースが存在しなくてもエラーにはしない。
    courseRepository.findCourseById(courseId).ifPresent(course ->
        courseRepository.deleteCourseById(courseId)
    );
  }
}