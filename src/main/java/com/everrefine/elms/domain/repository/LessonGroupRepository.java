package com.everrefine.elms.domain.repository;

import com.everrefine.elms.domain.model.lesson.LessonGroup;
import java.math.BigDecimal;
import java.util.Optional;

public interface LessonGroupRepository {

  LessonGroup createLessonGroup(LessonGroup lessonGroup);

  Optional<BigDecimal> findMaxLessonGroupOrderByCourseId(Integer courseId);

  Optional<LessonGroup> findLessonGroupById(Integer id);

  void deleteLessonGroupById(Integer id);
}
