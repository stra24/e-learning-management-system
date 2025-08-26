package com.everrefine.elms.infrastructure.dao;

import com.everrefine.elms.domain.model.course.Course;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonDao extends CrudRepository<Lesson, UUID> {
  
  @Autowired
  private JdbcTemplate jdbcTemplate;
  
  public Optional<UUID> findFirstLessonIdByCourseId(UUID courseId) {
    String sql = "SELECT id FROM lessons WHERE course_id = ? ORDER BY lesson_order ASC LIMIT 1";
    try {
      UUID result = jdbcTemplate.queryForObject(sql, UUID.class, courseId);
      return Optional.ofNullable(result);
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  Optional<Course> findTop1ByOrderByCourseOrderDesc();
}
