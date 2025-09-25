package com.everrefine.elms.infrastructure.dao;

import com.everrefine.elms.application.command.LessonSearchCommand;
import com.everrefine.elms.domain.model.lesson.Lesson;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jdbc.repository.query.Modifying;
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
    (:createdDateFrom IS NULL OR created_at >= CAST(:createdDateFrom AS DATE)) AND 
    (:createdDateTo IS NULL OR created_at < CAST(:createdDateTo AS DATE) + INTERVAL '1 day') 
    ORDER BY lesson_order ASC 
    LIMIT :limit OFFSET :offset
    """)
  List<Lesson> findLessons(
      @Param("courseId") Integer courseId,
      @Param("lessonGroupId") Integer lessonGroupId,
      @Param("title") String title,
      @Param("createdDateFrom") String createdDateFrom,
      @Param("createdDateTo") String createdDateTo,
      @Param("limit") int limit,
      @Param("offset") int offset
  );

  @Query("""
    SELECT COUNT(*) FROM lessons WHERE 
    (:courseId IS NULL OR course_id = :courseId) AND 
    (:lessonGroupId IS NULL OR lesson_group_id = :lessonGroupId) AND 
    (:title IS NULL OR title LIKE CONCAT('%', :title, '%')) AND 
    (:createdDateFrom IS NULL OR created_at >= CAST(:createdDateFrom AS DATE)) AND 
    (:createdDateTo IS NULL OR created_at < CAST(:createdDateTo AS DATE) + INTERVAL '1 day')
    """)
  int countLessons(
      @Param("courseId") Integer courseId,
      @Param("lessonGroupId") Integer lessonGroupId,
      @Param("title") String title,
      @Param("createdDateFrom") String createdDateFrom,
      @Param("createdDateTo") String createdDateTo
  );

  @Query("""
    SELECT * FROM lessons 
    WHERE lesson_group_id = :lessonGroupId 
    ORDER BY lesson_order ASC
    """)
  List<Lesson> findLessonsByLessonGroupId(@Param("lessonGroupId") Integer lessonGroupId);

  @Query("""
    SELECT MAX(lesson_order) 
    FROM lessons 
    WHERE lesson_group_id = :lessonGroupId
    """)
  Optional<BigDecimal> findMaxLessonOrderByLessonGroupId(@Param("lessonGroupId") Integer lessonGroupId);

  @Modifying
  @Query("""
    INSERT INTO lessons (id, lesson_group_id, course_id, lesson_order, title, description, video_url, created_at, updated_at)
    VALUES (:id, :lessonGroupId, :courseId, :lessonOrder, :title, :description, :videoUrl, :createdAt, :updatedAt)
    """)
  int insertLesson(
      @Param("id") Integer id,
      @Param("lessonGroupId") Integer lessonGroupId,
      @Param("courseId") Integer courseId,
      @Param("lessonOrder") BigDecimal lessonOrder,
      @Param("title") String title,
      @Param("description") String description,
      @Param("videoUrl") String videoUrl,
      @Param("createdAt") java.time.LocalDateTime createdAt,
      @Param("updatedAt") java.time.LocalDateTime updatedAt
  );
  
  @Query("SELECT * FROM lessons ORDER BY id DESC LIMIT 1")
  Optional<Lesson> findLatestLesson();
}
