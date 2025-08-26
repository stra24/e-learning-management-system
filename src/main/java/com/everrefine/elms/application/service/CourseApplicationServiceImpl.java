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
import com.everrefine.elms.domain.model.course.CourseForCreateRequest;
import com.everrefine.elms.domain.model.course.CourseForUpdateRequest;
import com.everrefine.elms.domain.model.course.Description;
import com.everrefine.elms.domain.model.course.Title;
import com.everrefine.elms.domain.repository.CourseRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    return CourseDtoConverter.toDto(course);
  }

  @Override
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
  public void createCourse(CourseCreateCommand courseCreateCommand) {
    LocalDateTime now = LocalDateTime.now();
    CourseForCreateRequest course = new CourseForCreateRequest(
        null,
        new Title(courseCreateCommand.getTitle()),
        new Description(courseCreateCommand.getDescription()),
        new Url(courseCreateCommand.getThumbnailUrl()),
        now,
        now
    );
    courseRepository.createCourse(course);
  }

  @Override
  public void updateCourse(CourseUpdateCommand courseUpdateCommand) {
    CourseDto courseDto = findCourseById(courseUpdateCommand.getId().toString());
    CourseForUpdateRequest course = new CourseForUpdateRequest(
        courseDto.getId(),
        new Order(courseUpdateCommand.getCourseOrder()),
        new Title(courseUpdateCommand.getTitle()),
        new Description(courseUpdateCommand.getDescription()),
        new Url(courseUpdateCommand.getThumbnailUrl())
    );
    courseRepository.updateCourse(course);
  }

  @Override
  public void deleteCourseById(String courseId) {
    UUID uuid = UUID.fromString(courseId);
    // コースが存在しなくてもエラーにはしない。
    courseRepository.findCourseById(uuid).ifPresent(course ->
        courseRepository.deleteCourseById(uuid)
    );
  }
}