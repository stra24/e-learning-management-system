package com.everrefine.elms.application.service;

import com.everrefine.elms.application.dto.FirstLessonDto;
import com.everrefine.elms.domain.repository.LessonRepository;
import io.micrometer.common.util.StringUtils;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LessonApplicationServiceImpl implements LessonApplicationService {

  private final LessonRepository lessonRepository;

  @Override
  public FirstLessonDto findFirstLessonIdByCourseId(String courseId) {
    if (StringUtils.isBlank(courseId)) {
      return new FirstLessonDto(false, null);
    }

    try {
      UUID courseUuid = UUID.fromString(courseId);
      return lessonRepository.findFirstLessonIdByCourseId(courseUuid)
          .map(uuid -> new FirstLessonDto(true, uuid))
          .orElseGet(() -> new FirstLessonDto(false, null));
    } catch (IllegalArgumentException e) {
      return new FirstLessonDto(false, null);
    }
  }
}