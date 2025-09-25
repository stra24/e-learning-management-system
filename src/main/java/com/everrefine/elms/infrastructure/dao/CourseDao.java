package com.everrefine.elms.infrastructure.dao;

import com.everrefine.elms.domain.model.course.Course;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseDao extends CrudRepository<Course, Integer> {

  @Query("""
        SELECT * 
        FROM courses 
        ORDER BY course_order ASC 
        LIMIT :pageSize 
        OFFSET :offset
      """)
  List<Course> findCoursesWithPagination(@Param("pageSize") int pageSize,
      @Param("offset") int offset);

  @Query("""
        SELECT COUNT(*) 
        FROM courses
      """)
  int countAllCourses();

  @Modifying
  @Query("UPDATE courses SET course_order = :courseOrder, title = :title, description = :description, thumbnail_url = :thumbnailUrl, updated_at = now() WHERE id = :id")
  int updateCourseFields(
      @Param("id") Integer id,
      @Param("courseOrder") BigDecimal courseOrder,
      @Param("title") String title,
      @Param("description") String description,
      @Param("thumbnailUrl") String thumbnailUrl
  );

  Optional<Course> findTop1ByOrderByCourseOrderDesc();
}
