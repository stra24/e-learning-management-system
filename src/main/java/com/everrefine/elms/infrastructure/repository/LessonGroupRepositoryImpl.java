package com.everrefine.elms.infrastructure.repository;

import com.everrefine.elms.domain.model.lesson.LessonGroup;
import com.everrefine.elms.domain.repository.LessonGroupRepository;
import com.everrefine.elms.infrastructure.dao.LessonGroupDao;
import java.math.BigDecimal;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class LessonGroupRepositoryImpl implements LessonGroupRepository {

  private final LessonGroupDao lessonGroupDao;

  @Override
  public LessonGroup createLessonGroup(LessonGroup lessonGroup) {
    return lessonGroupDao.save(lessonGroup);
  }

  @Override
  public Optional<BigDecimal> findMaxLessonGroupOrderByCourseId(Integer courseId) {
    return lessonGroupDao.findMaxLessonGroupOrderByCourseId(courseId);
  }
}
