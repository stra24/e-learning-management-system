package com.everrefine.elms.infrastructure.dao;

import com.everrefine.elms.domain.model.lesson.Lesson;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonDao extends CrudRepository<Lesson, Integer> {
  @Query("""
    SELECT l.*
    FROM lessons l
    JOIN lesson_groups g ON g.id = l.lesson_group_id
    WHERE l.course_id = :courseId
    ORDER BY g.lesson_group_order ASC, l.lesson_order ASC
    LIMIT 1
  """)
  Optional<Lesson> findFirstLessonByCourseId(Integer courseId);

  @Query("""
    SELECT * FROM lessons WHERE 
    (:courseId IS NULL OR course_id = :courseId) AND 
    (:lessonGroupId IS NULL OR lesson_group_id = :lessonGroupId) AND 
    (:title IS NULL OR title LIKE CONCAT('%', :title, '%')) AND 
    (CAST(:createdDateFrom AS DATE) IS NULL OR created_at >= CAST(:createdDateFrom AS DATE)) AND 
    (CAST(:createdDateTo AS DATE) IS NULL OR created_at < CAST(:createdDateTo AS DATE) + INTERVAL '1 day') 
    ORDER BY lesson_order ASC 
    LIMIT :limit OFFSET :offset
    """)
  List<Lesson> findLessons(
      @Param("courseId") Integer courseId,
      @Param("lessonGroupId") Integer lessonGroupId,
      @Param("title") String title,
      @Param("createdDateFrom") LocalDate createdDateFrom,
      @Param("createdDateTo") LocalDate createdDateTo,
      @Param("limit") int limit,
      @Param("offset") int offset
  );

  @Query("""
    SELECT COUNT(*) FROM lessons WHERE 
    (:courseId IS NULL OR course_id = :courseId) AND 
    (:lessonGroupId IS NULL OR lesson_group_id = :lessonGroupId) AND 
    (:title IS NULL OR title LIKE CONCAT('%', :title, '%')) AND 
    (CAST(:createdDateFrom AS DATE) IS NULL OR created_at >= CAST(:createdDateFrom AS DATE)) AND 
    (CAST(:createdDateTo AS DATE) IS NULL OR created_at < CAST(:createdDateTo AS DATE) + INTERVAL '1 day')
    """)
  int countLessons(
      @Param("courseId") Integer courseId,
      @Param("lessonGroupId") Integer lessonGroupId,
      @Param("title") String title,
      @Param("createdDateFrom") LocalDate createdDateFrom,
      @Param("createdDateTo") LocalDate createdDateTo
  );

  @Query("""
    SELECT MAX(lesson_order) 
    FROM lessons 
    WHERE lesson_group_id = :lessonGroupId
    """)
  Optional<BigDecimal> findMaxLessonOrderByLessonGroupId(@Param("lessonGroupId") Integer lessonGroupId);
}
