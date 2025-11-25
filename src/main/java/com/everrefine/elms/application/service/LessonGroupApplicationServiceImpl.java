package com.everrefine.elms.application.service;

import com.everrefine.elms.application.command.LessonGroupCreateCommand;
import com.everrefine.elms.application.dto.LessonGroupDto;
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
  public void deleteLessonGroupById(Integer lessonGroupId) {
    // ユーザーが存在しなくてもエラーにはしない。
    lessonGroupRepository.findLessonGroupById(lessonGroupId).ifPresent(LessonGroup ->
        lessonGroupRepository.deleteLessonGroupById(lessonGroupId)
    );
  }
}
