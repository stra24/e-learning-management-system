package com.everrefine.elms.domain.repository;

import com.everrefine.elms.domain.model.lesson.Lesson;
import java.util.Optional;
import java.util.UUID;

public interface LessonRepository {

  Optional<UUID> findFirstLessonIdByCourseId(UUID courseId);

  Optional<Lesson> findById(UUID lessonId);
}
