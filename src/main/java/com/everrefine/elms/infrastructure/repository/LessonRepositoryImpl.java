package com.everrefine.elms.infrastructure.repository;

import com.everrefine.elms.application.command.LessonSearchCommand;
import com.everrefine.elms.application.dto.CourseLessonsDto;
import com.everrefine.elms.application.dto.LessonDto;
import com.everrefine.elms.application.dto.LessonGroupDto;
import com.everrefine.elms.application.command.LessonCreateCommand;
import com.everrefine.elms.domain.model.lesson.Lesson;
import com.everrefine.elms.domain.model.lesson.LessonGroup;
import com.everrefine.elms.domain.model.lesson.LessonGroupWithLesson;
import com.everrefine.elms.domain.repository.LessonRepository;
import com.everrefine.elms.infrastructure.dao.LessonDao;
import com.everrefine.elms.infrastructure.dao.LessonGroupDao;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class LessonRepositoryImpl implements LessonRepository {

  private final LessonDao lessonDao;
  private final LessonGroupDao lessonGroupDao;

  @Override
  public Optional<Lesson> findFirstLessonByCourseId(Integer courseId) {
    return lessonDao.findFirstLessonByCourseId(courseId);
  }

  @Override
  public Optional<Lesson> findById(Integer lessonId) {
    return lessonDao.findById(lessonId);
  }

  @Override
  public List<Lesson> findLessons(LessonSearchCommand lessonSearchCommand) {
    Integer courseId = lessonSearchCommand.getCourseId() != null ? 
        Integer.valueOf(lessonSearchCommand.getCourseId()) : null;
    Integer lessonGroupId = lessonSearchCommand.getLessonGroupId() != null ? 
        Integer.valueOf(lessonSearchCommand.getLessonGroupId()) : null;
    LocalDate createdDateFrom = lessonSearchCommand.getCreatedDateFrom() != null ?
        lessonSearchCommand.getCreatedDateFrom() : null;
    LocalDate createdDateTo = lessonSearchCommand.getCreatedDateTo() != null ?
        lessonSearchCommand.getCreatedDateTo() : null;
    
    int offset = (lessonSearchCommand.getPageNum() - 1) * lessonSearchCommand.getPageSize();
    
    return lessonDao.findLessons(
        courseId,
        lessonGroupId,
        lessonSearchCommand.getTitle(),
        createdDateFrom,
        createdDateTo,
        lessonSearchCommand.getPageSize(),
        offset
    );
  }

  @Override
  public int countLessons(LessonSearchCommand lessonSearchCommand) {
    Integer courseId = lessonSearchCommand.getCourseId() != null ? 
        Integer.valueOf(lessonSearchCommand.getCourseId()) : null;
    Integer lessonGroupId = lessonSearchCommand.getLessonGroupId() != null ? 
        Integer.valueOf(lessonSearchCommand.getLessonGroupId()) : null;
    LocalDate createdDateFrom = lessonSearchCommand.getCreatedDateFrom() != null ?
        lessonSearchCommand.getCreatedDateFrom() : null;
    LocalDate createdDateTo = lessonSearchCommand.getCreatedDateTo() != null ?
        lessonSearchCommand.getCreatedDateTo() : null;
    
    return lessonDao.countLessons(
        courseId,
        lessonGroupId,
        lessonSearchCommand.getTitle(),
        createdDateFrom,
        createdDateTo
    );
  }

  @Override
  public CourseLessonsDto findLessonsGroupedByLessonGroup(Integer courseId) {
    List<LessonGroupWithLesson> results = lessonGroupDao.findLessonGroupsByCourseId(courseId);
    
    Map<Integer, List<LessonGroupWithLesson>> lessonGroupIdAndLessonsMap = results.stream()
        .collect(Collectors.groupingBy(LessonGroupWithLesson::getLessonGroupId));
    
    List<LessonGroupDto> lessonGroups = lessonGroupIdAndLessonsMap.values().stream()
        .map(this::createLessonGroupDto)
        .toList();
    
    return new CourseLessonsDto(courseId, lessonGroups);
  }

  @Override
  public Lesson createLesson(Lesson lesson) {
    return lessonDao.save(lesson);
  }

  @Override
  public Optional<BigDecimal> findMaxLessonOrderByLessonGroupId(Integer lessonGroupId) {
    return lessonDao.findMaxLessonOrderByLessonGroupId(lessonGroupId);
  }
  
  private LessonGroupDto createLessonGroupDto(List<LessonGroupWithLesson> lessons) {
    LessonGroupWithLesson first = lessons.getFirst();
    
    List<LessonDto> lessonDtos = lessons.stream()
        .filter(lesson -> lesson.getLessonId() != null)
        .map(this::createLessonDto)
        .toList();
    
    return new LessonGroupDto(
        first.getLessonGroupId(),
        first.getCourseId(),
        first.getLessonGroupOrder(),
        first.getLessonGroupTitle(),
        first.getLessonGroupCreatedAt(),
        first.getLessonGroupUpdatedAt(),
        lessonDtos
    );
  }
  
  private LessonDto createLessonDto(LessonGroupWithLesson lesson) {
    return new LessonDto(
        lesson.getLessonId(),
        lesson.getLessonGroupId(),
        lesson.getCourseId(),
        lesson.getLessonOrder(),
        lesson.getLessonTitle(),
        lesson.getLessonDescription(),
        lesson.getLessonVideoUrl(),
        lesson.getLessonCreatedAt(),
        lesson.getLessonUpdatedAt()
    );
  }
}