package com.everrefine.elms.infrastructure.dao;

import com.everrefine.elms.domain.model.lesson.Lesson;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonDao extends CrudRepository<Lesson, UUID> {
  Optional<Lesson> findTop1ByCourseIdOrderByLessonOrderDesc(UUID courseId);
}
