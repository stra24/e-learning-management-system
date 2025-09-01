package com.everrefine.elms.infrastructure.dao;

import com.everrefine.elms.application.command.LessonSearchCommand;
import com.everrefine.elms.domain.model.lesson.Lesson;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonDao extends CrudRepository<Lesson, UUID> {
  Optional<Lesson> findTop1ByCourseIdOrderByLessonOrderDesc(UUID courseId);

  @Query("""
    SELECT * FROM lessons WHERE 
    (:courseId IS NULL OR course_id = CAST(:courseId AS UUID)) AND 
    (:lessonGroupId IS NULL OR lesson_group_id = CAST(:lessonGroupId AS UUID)) AND 
    (:title IS NULL OR title LIKE CONCAT('%', :title, '%')) AND 
    (:createdDateFrom IS NULL OR created_at >= CAST(:createdDateFrom AS DATE)) AND 
    (:createdDateTo IS NULL OR created_at < CAST(:createdDateTo AS DATE) + INTERVAL '1 day') 
    ORDER BY lesson_order ASC 
    LIMIT :limit OFFSET :offset
    """)
  List<Lesson> findLessons(
      @Param("courseId") UUID courseId,
      @Param("lessonGroupId") UUID lessonGroupId,
      @Param("title") String title,
      @Param("createdDateFrom") String createdDateFrom,
      @Param("createdDateTo") String createdDateTo,
      @Param("limit") int limit,
      @Param("offset") int offset
  );

  @Query("""
    SELECT COUNT(*) FROM lessons WHERE 
    (:courseId IS NULL OR course_id = CAST(:courseId AS UUID)) AND 
    (:lessonGroupId IS NULL OR lesson_group_id = CAST(:lessonGroupId AS UUID)) AND 
    (:title IS NULL OR title LIKE CONCAT('%', :title, '%')) AND 
    (:createdDateFrom IS NULL OR created_at >= CAST(:createdDateFrom AS DATE)) AND 
    (:createdDateTo IS NULL OR created_at < CAST(:createdDateTo AS DATE) + INTERVAL '1 day')
    """)
  int countLessons(
      @Param("courseId") UUID courseId,
      @Param("lessonGroupId") UUID lessonGroupId,
      @Param("title") String title,
      @Param("createdDateFrom") String createdDateFrom,
      @Param("createdDateTo") String createdDateTo
  );

  @Query("""
    SELECT * FROM lessons 
    WHERE lesson_group_id = :lessonGroupId 
    ORDER BY lesson_order ASC
    """)
  List<Lesson> findLessonsByLessonGroupId(@Param("lessonGroupId") UUID lessonGroupId);
}
