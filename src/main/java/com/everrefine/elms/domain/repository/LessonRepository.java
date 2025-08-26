package com.everrefine.elms.domain.repository;

import java.util.Optional;
import java.util.UUID;

public interface LessonRepository {

  Optional<UUID> findFirstLessonIdByCourseId(UUID courseId);
}
