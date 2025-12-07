package com.everrefine.elms.application.service;

import com.everrefine.elms.application.command.LessonCreateCommand;
import com.everrefine.elms.application.command.LessonSearchCommand;
import com.everrefine.elms.application.command.LessonUpdateCommand;
import com.everrefine.elms.application.dto.CourseLessonsDto;
import com.everrefine.elms.application.dto.FirstLessonDto;
import com.everrefine.elms.application.dto.LessonDto;
import com.everrefine.elms.application.dto.LessonPageDto;
import com.everrefine.elms.domain.model.lesson.Lesson;
import com.everrefine.elms.domain.repository.LessonRepository;
import com.everrefine.elms.domain.service.LessonDomainService;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class LessonApplicationServiceImpl implements LessonApplicationService {

  private final LessonRepository lessonRepository;
  private final LessonDomainService lessonDomainService;

  @Override
  @Transactional(readOnly = true)
  public FirstLessonDto findFirstLessonIdByCourseId(Integer courseId) {
    if (courseId == null) {
      return new FirstLessonDto(false, null, null);
    }

    return lessonRepository.findFirstLessonByCourseId(courseId)
        .map(lesson -> new FirstLessonDto(true, lesson.getLessonGroupId(), lesson.getId()))
        .orElseGet(() -> new FirstLessonDto(false, null, null));
  }

  @Override
  @Transactional(readOnly = true)
  public LessonDto findLessonById(Integer courseId, Integer lessonId) {
    Lesson lesson = lessonRepository.findById(lessonId)
        .orElseThrow(() -> new IllegalArgumentException("Lesson not found"));

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
  @Transactional(readOnly = true)
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
  @Transactional(readOnly = true)
  public CourseLessonsDto findLessonsGroupedByLessonGroup(Integer courseId) {
    return lessonRepository.findLessonsGroupedByLessonGroup(courseId);
  }

  @Override
  @Transactional
  public LessonDto createLesson(LessonCreateCommand lessonCreateCommand) {
    // レッスンの並び順を自動発番
    BigDecimal lessonOrder = lessonDomainService.issueLessonOrder(
        lessonCreateCommand.getLessonGroupId());

    Lesson lesson = Lesson.create(
        lessonCreateCommand.getLessonGroupId(),
        lessonCreateCommand.getCourseId(),
        lessonOrder,
        lessonCreateCommand.getTitle(),
        lessonCreateCommand.getDescription(),
        lessonCreateCommand.getVideoUrl()
    );

    Lesson createdLesson = lessonRepository.createLesson(lesson);

    return new LessonDto(
        createdLesson.getId(),
        createdLesson.getLessonGroupId(),
        createdLesson.getCourseId(),
        lessonOrder,
        createdLesson.getTitle().getValue(),
        createdLesson.getDescription() != null ? createdLesson.getDescription().getValue() : null,
        createdLesson.getVideoUrl() != null ? createdLesson.getVideoUrl().getValue() : null,
        createdLesson.getCreatedAt(),
        createdLesson.getUpdatedAt()
    );
  }

  @Override
  @Transactional
  public LessonDto updateLesson(LessonUpdateCommand lessonUpdateCommand) {
    Lesson currentLesson = lessonRepository.findById(lessonUpdateCommand.getId())
        .orElseThrow(
            () -> new RuntimeException("Lesson not found with ID: " + lessonUpdateCommand.getId()));

    Lesson updateLesson = currentLesson.update(
        lessonUpdateCommand.getTitle(),
        lessonUpdateCommand.getDescription(),
        lessonUpdateCommand.getVideoUrl()
    );

    Lesson updatedLesson = lessonRepository.updateLesson(updateLesson);

    return new LessonDto(
        updatedLesson.getId(),
        updatedLesson.getLessonGroupId(),
        updatedLesson.getCourseId(),
        updatedLesson.getLessonOrder().getValue(),
        updatedLesson.getTitle().getValue(),
        updatedLesson.getDescription() != null ? updatedLesson.getDescription().getValue() : null,
        updatedLesson.getVideoUrl() != null ? updatedLesson.getVideoUrl().getValue() : null,
        updatedLesson.getCreatedAt(),
        updatedLesson.getUpdatedAt()
    );
  }

  @Override
  @Transactional
  public void deleteLessonById(Integer lessonId) {
    lessonRepository.findById(lessonId)
        .ifPresent(lesson -> lessonRepository.deleteLessonById(lessonId));
  }
}
