package com.everrefine.elms.application.service;

import com.everrefine.elms.application.command.LessonCreateCommand;
import com.everrefine.elms.application.command.LessonSearchCommand;
import com.everrefine.elms.application.dto.CourseLessonsDto;
import com.everrefine.elms.application.dto.FirstLessonDto;
import com.everrefine.elms.application.dto.LessonDto;
import com.everrefine.elms.application.dto.LessonPageDto;
import com.everrefine.elms.domain.model.lesson.Lesson;
import com.everrefine.elms.domain.repository.LessonRepository;
import com.everrefine.elms.domain.service.LessonDomainService;
import io.micrometer.common.util.StringUtils;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LessonApplicationServiceImpl implements LessonApplicationService {

  private final LessonRepository lessonRepository;
  private final LessonDomainService lessonDomainService;

  @Override
  public FirstLessonDto findFirstLessonIdByCourseId(String courseId) {
    if (StringUtils.isBlank(courseId)) {
      return new FirstLessonDto(false, null, null);
    }

    try {
      UUID courseUuid = UUID.fromString(courseId);
      return lessonRepository.findFirstLessonByCourseId(courseUuid)
          .map(lesson -> new FirstLessonDto(true, lesson.getLessonGroupId(), lesson.getId()))
          .orElseGet(() -> new FirstLessonDto(false, null, null));
    } catch (IllegalArgumentException e) {
      return new FirstLessonDto(false, null, null);
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

  @Override
  public LessonDto createLesson(LessonCreateCommand lessonCreateCommand) {
    // レッスンの並び順を自動発番
    BigDecimal lessonOrder = lessonDomainService.issueLessonOrder(lessonCreateCommand.getLessonGroupId());
    
    // 発番した並び順をコマンドにセットする
    LessonCreateCommand commandWithOrder = lessonCreateCommand.updateLessonOrder(lessonOrder);
    
    Lesson createdLesson = lessonRepository.createLesson(commandWithOrder);
    
    return new LessonDto(
        createdLesson.getId(),
        createdLesson.getLessonGroupId(),
        createdLesson.getCourseId(),
        lessonOrder, // 発番された並び順を使用
        createdLesson.getTitle().getValue(),
        createdLesson.getDescription() != null ? createdLesson.getDescription().getValue() : null,
        createdLesson.getVideoUrl() != null ? createdLesson.getVideoUrl().getValue() : null,
        createdLesson.getCreatedAt(),
        createdLesson.getUpdatedAt()
    );
  }
}