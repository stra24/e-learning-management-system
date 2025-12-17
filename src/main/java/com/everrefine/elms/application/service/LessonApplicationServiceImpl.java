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

    return LessonDto.from(lesson);
  }

  @Override
  @Transactional(readOnly = true)
  public LessonPageDto findLessons(LessonSearchCommand lessonSearchCommand) {
    List<Lesson> lessons = lessonRepository.findLessons(lessonSearchCommand);
    int totalSize = lessonRepository.countLessons(lessonSearchCommand);

    List<LessonDto> lessonDtos = lessons.stream()
        .map(LessonDto::from)
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

    return LessonDto.from(createdLesson);
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

    return LessonDto.from(updatedLesson);
  }

  @Override
  @Transactional
  public void deleteLessonById(Integer lessonId) {
    lessonRepository.findById(lessonId)
        .ifPresent(lesson -> lessonRepository.deleteLessonById(lessonId));
  }
}
