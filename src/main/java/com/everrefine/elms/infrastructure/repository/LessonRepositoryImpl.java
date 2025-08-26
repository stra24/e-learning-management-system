package com.everrefine.elms.infrastructure.repository;

import com.everrefine.elms.domain.repository.LessonRepository;
import com.everrefine.elms.infrastructure.mapper.LessonMapper;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class LessonRepositoryImpl implements LessonRepository {

  private final LessonMapper lessonMapper;

  @Override
  public Optional<UUID> findFirstLessonIdByCourseId(UUID courseId) {
    return lessonMapper.findFirstLessonIdByCourseId(courseId);
  }
}