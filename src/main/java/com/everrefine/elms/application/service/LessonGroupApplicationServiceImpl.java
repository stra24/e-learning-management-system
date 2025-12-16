package com.everrefine.elms.application.service;

import com.everrefine.elms.application.command.LessonGroupCreateCommand;
import com.everrefine.elms.application.command.LessonGroupUpdateCommand;
import com.everrefine.elms.application.dto.LessonGroupDto;
import com.everrefine.elms.application.exception.ResourceNotFoundException;
import com.everrefine.elms.domain.model.lesson.LessonGroup;
import com.everrefine.elms.domain.repository.LessonGroupRepository;
import com.everrefine.elms.domain.service.LessonGroupDomainService;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class LessonGroupApplicationServiceImpl implements LessonGroupApplicationService {

  private final LessonGroupRepository lessonGroupRepository;
  private final LessonGroupDomainService lessonGroupDomainService;

  @Override
  @Transactional
  public LessonGroupDto createLessonGroup(LessonGroupCreateCommand lessonGroupCreateCommand) {
    // レッスングループの並び順を自動発番
    BigDecimal lessonGroupOrder = lessonGroupDomainService.issueLessonGroupOrder(
        lessonGroupCreateCommand.getCourseId());

    LessonGroup lessonGroup = LessonGroup.create(
        lessonGroupCreateCommand.getCourseId(),
        lessonGroupOrder,
        lessonGroupCreateCommand.getTitle()
    );

    LessonGroup createdLessonGroup = lessonGroupRepository.createLessonGroup(lessonGroup);

    return new LessonGroupDto(
        createdLessonGroup.getId(),
        createdLessonGroup.getCourseId(),
        createdLessonGroup.getLessonGroupOrder().getValue(),
        createdLessonGroup.getTitle().getValue(),
        createdLessonGroup.getCreatedAt(),
        createdLessonGroup.getUpdatedAt(),
        null
    );
  }

  @Override
  @Transactional
  public LessonGroupDto updateLessonGroup(LessonGroupUpdateCommand lessonGroupUpdateCommand,
      Integer courseId) {
    LessonGroup lessonGroup = lessonGroupRepository.findLessonGroupById(lessonGroupUpdateCommand.getId())
        .orElseThrow(() -> new ResourceNotFoundException("LessonGroup not found"));

    // Validate that the lesson group belongs to the specified course
    if (!lessonGroup.getCourseId().equals(courseId)) {
      throw new ResourceNotFoundException("LessonGroup does not belong to the specified course");
    }

    LessonGroup updatedLessonGroup = lessonGroupDomainService.updateLessonGroup(
        lessonGroup, lessonGroupUpdateCommand.getTitle());

    return new LessonGroupDto(
        updatedLessonGroup.getId(),
        updatedLessonGroup.getCourseId(),
        updatedLessonGroup.getLessonGroupOrder().getValue(),
        updatedLessonGroup.getTitle().getValue(),
        updatedLessonGroup.getCreatedAt(),
        updatedLessonGroup.getUpdatedAt(),
        null
    );
  }

  @Override
  @Transactional
  public void deleteLessonGroupById(Integer lessonGroupId) {
    // レッスングループが存在しなくてもエラーにはしない。
    lessonGroupRepository.findLessonGroupById(lessonGroupId).ifPresent(LessonGroup ->
        lessonGroupRepository.deleteLessonGroupById(lessonGroupId)
    );
  }
}
