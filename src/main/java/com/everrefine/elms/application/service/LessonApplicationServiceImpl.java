package com.everrefine.elms.application.service;

import com.everrefine.elms.application.command.LessonSearchCommand;
import com.everrefine.elms.application.dto.CourseLessonsDto;
import com.everrefine.elms.application.dto.FirstLessonDto;
import com.everrefine.elms.application.dto.LessonDto;
import com.everrefine.elms.application.dto.LessonPageDto;
import com.everrefine.elms.domain.model.lesson.Lesson;
import com.everrefine.elms.domain.repository.LessonRepository;
import io.micrometer.common.util.StringUtils;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
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

  @Override
  public LessonDto findLessonById(String courseId, String lessonId) {
    UUID courseUuid = UUID.fromString(courseId);
    UUID lessonUuid = UUID.fromString(lessonId);

    Lesson lesson = lessonRepository.findById(lessonUuid)
        .orElseThrow(() -> new IllegalArgumentException("Lesson not found"));

    // コースIDが一致するかチェック
    if (!lesson.getCourseId().equals(courseUuid)) {
      throw new IllegalArgumentException("Lesson does not belong to the specified course");
    }

    return new LessonDto(
        lesson.getId(),
        lesson.getLessonGroupId(),
        lesson.getCourseId(),
        lesson.getLessonOrder().getValue(),
        lesson.getTitle().getValue(),
        lesson.getDescription() != null ? lesson.getDescription().getValue() : null,
        lesson.getVideoUrl() != null ? lesson.getVideoUrl().getValue() : null,
        lesson.getCreatedAt(),
        lesson.getUpdatedAt()
    );
  }

  @Override
  public LessonPageDto findLessons(LessonSearchCommand lessonSearchCommand) {
    List<Lesson> lessons = lessonRepository.findLessons(lessonSearchCommand);
    int totalSize = lessonRepository.countLessons(lessonSearchCommand);

    List<LessonDto> lessonDtos = lessons.stream()
        .map(lesson -> new LessonDto(
            lesson.getId(),
            lesson.getLessonGroupId(),
            lesson.getCourseId(),
            lesson.getLessonOrder().getValue(),
            lesson.getTitle().getValue(),
            lesson.getDescription() != null ? lesson.getDescription().getValue() : null,
            lesson.getVideoUrl() != null ? lesson.getVideoUrl().getValue() : null,
            lesson.getCreatedAt(),
            lesson.getUpdatedAt()
        ))
        .collect(Collectors.toList());

    return new LessonPageDto(
        lessonDtos,
        lessonSearchCommand.getPageNum(),
        lessonSearchCommand.getPageSize(),
        totalSize
    );
  }

  @Override
  public CourseLessonsDto findLessonsGroupedByLessonGroup(String courseId) {
    try {
      UUID courseUuid = UUID.fromString(courseId);
      return lessonRepository.findLessonsGroupedByLessonGroup(courseUuid);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Invalid course ID format");
    }
  }
}