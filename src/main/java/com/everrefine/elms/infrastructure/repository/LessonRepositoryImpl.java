package com.everrefine.elms.infrastructure.repository;

import com.everrefine.elms.domain.model.lesson.Lesson;
import com.everrefine.elms.domain.repository.LessonRepository;
import com.everrefine.elms.infrastructure.dao.LessonDao;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class LessonRepositoryImpl implements LessonRepository {

  private final LessonDao lessonDao;

  @Override
  public Optional<UUID> findFirstLessonIdByCourseId(UUID courseId) {
    return lessonDao.findTop1ByCourseIdOrderByLessonOrderDesc(courseId).map(Lesson::getCourseId);
  }
}