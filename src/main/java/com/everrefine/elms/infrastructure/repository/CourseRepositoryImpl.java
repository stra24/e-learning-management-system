package com.everrefine.elms.infrastructure.repository;

import static com.everrefine.elms.domain.model.Order.getFirst;

import com.everrefine.elms.domain.model.Order;
import com.everrefine.elms.domain.model.course.Course;
import com.everrefine.elms.domain.model.PagerForRequest;
import com.everrefine.elms.domain.model.course.CourseForCreateRequest;
import com.everrefine.elms.domain.model.course.CourseForUpdateRequest;
import com.everrefine.elms.domain.repository.CourseRepository;
import com.everrefine.elms.infrastructure.dao.CourseDao;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class CourseRepositoryImpl implements CourseRepository {

  private final CourseDao courseDao;

  @Override
  public void updateCourse(CourseForUpdateRequest course) {
    courseDao.updateCourseFields(
        course.getId(),
        course.getCourseOrder().getValue(),
        course.getTitle().getValue(),
        course.getDescription() != null ? course.getDescription().getValue() : null,
        course.getThumbnailUrl() != null ? course.getThumbnailUrl().getValue() : null
    );
  }

  @Override
  public void createCourse(CourseForCreateRequest course) {
    Course newCourse = new Course(
        course.getId(),
        course.getThumbnailUrl(),
        course.getTitle(),
        course.getDescription(),
        issueCourseOrder(),
        course.getCreatedAt(),
        course.getUpdatedAt()
    );
    courseDao.save(newCourse);
  }

  @Override
  public void deleteCourseById(UUID id) {
    courseDao.deleteById(id);
  }
  
  @Override
  public Optional<Course> findCourseById(UUID id) {
    return courseDao.findById(id);
  }

  @Override
  public List<Course> findCourses(PagerForRequest pagerForRequest) {
    return courseDao.findCoursesWithPagination(
        pagerForRequest.getPageSize(),
        pagerForRequest.getOffset()
    );
  }

  @Override
  public int countCourses() {
    return courseDao.countAllCourses();
  }

  @Override
  public Order issueCourseOrder() {
    return courseDao.findTop1ByOrderByCourseOrderDesc()
        .map(course -> course.getCourseOrder().getNext())
        .orElse(getFirst());
  }
}