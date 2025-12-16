package com.everrefine.elms.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.everrefine.elms.application.command.LessonGroupCreateCommand;
import com.everrefine.elms.application.command.LessonGroupUpdateCommand;
import com.everrefine.elms.application.dto.LessonGroupDto;
import com.everrefine.elms.application.exception.ResourceNotFoundException;
import com.everrefine.elms.domain.model.lesson.LessonGroup;
import com.everrefine.elms.domain.repository.LessonGroupRepository;
import com.everrefine.elms.domain.service.LessonGroupDomainService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LessonGroupApplicationServiceImplTest {

  @Mock
  private LessonGroupRepository lessonGroupRepository;

  @Mock
  private LessonGroupDomainService lessonGroupDomainService;

  @InjectMocks
  private LessonGroupApplicationServiceImpl lessonGroupApplicationService;

  private LessonGroup existingLessonGroup;
  private LessonGroup updatedLessonGroup;

  @BeforeEach
  void setUp() {
    existingLessonGroup = new LessonGroup(
        1,
        100,
        new com.everrefine.elms.domain.model.Order(new BigDecimal("1")),
        new com.everrefine.elms.domain.model.lesson.Title("元のタイトル"),
        LocalDateTime.of(2025, 12, 10, 10, 0),
        LocalDateTime.of(2025, 12, 10, 10, 0)
    );

    updatedLessonGroup = new LessonGroup(
        1,
        100,
        new com.everrefine.elms.domain.model.Order(new BigDecimal("1")),
        new com.everrefine.elms.domain.model.lesson.Title("新しいタイトル"),
        LocalDateTime.of(2025, 12, 10, 10, 0),
        LocalDateTime.of(2025, 12, 11, 11, 0)
    );
  }

  @Test
  void 正常系_レッスングループを更新できること() {
    // Arrange
    LessonGroupUpdateCommand command = LessonGroupUpdateCommand.create(1, "新しいタイトル");
    when(lessonGroupRepository.findLessonGroupById(1))
        .thenReturn(Optional.of(existingLessonGroup));
    when(lessonGroupDomainService.updateLessonGroup(existingLessonGroup, "新しいタイトル"))
        .thenReturn(updatedLessonGroup);

    // Act
    LessonGroupDto result = lessonGroupApplicationService.updateLessonGroup(command, 100);

    // Assert
    assertEquals(1, result.id());
    assertEquals(100, result.courseId());
    assertEquals(new BigDecimal("1"), result.lessonGroupOrder());
    assertEquals("新しいタイトル", result.name());
    assertEquals(LocalDateTime.of(2025, 12, 10, 10, 0), result.createdAt());
    assertEquals(LocalDateTime.of(2025, 12, 11, 11, 0), result.updatedAt());

    verify(lessonGroupRepository, times(1)).findLessonGroupById(1);
    verify(lessonGroupDomainService, times(1)).updateLessonGroup(existingLessonGroup, "新しいタイトル");
  }

  @Test
  void 異常系_存在しないレッスングループIDの場合ResourceNotFoundExceptionがスローされること() {
    // Arrange
    LessonGroupUpdateCommand command = LessonGroupUpdateCommand.create(999, "新しいタイトル");
    when(lessonGroupRepository.findLessonGroupById(999))
        .thenReturn(Optional.empty());

    // Act & Assert
    ResourceNotFoundException exception = assertThrows(
        ResourceNotFoundException.class,
        () -> lessonGroupApplicationService.updateLessonGroup(command, 100)
    );
    assertEquals("LessonGroup not found", exception.getMessage());

    verify(lessonGroupRepository, times(1)).findLessonGroupById(999);
    verify(lessonGroupDomainService, never()).updateLessonGroup(any(), any());
  }

  @Test
  void 異常系_指定されたコースIDにレッスングループが属していない場合ResourceNotFoundExceptionがスローされること() {
    // Arrange
    LessonGroupUpdateCommand command = LessonGroupUpdateCommand.create(1, "新しいタイトル");
    when(lessonGroupRepository.findLessonGroupById(1))
        .thenReturn(Optional.of(existingLessonGroup));

    // Act & Assert
    ResourceNotFoundException exception = assertThrows(
        ResourceNotFoundException.class,
        () -> lessonGroupApplicationService.updateLessonGroup(command, 200) // 異なるcourseId
    );
    assertEquals("LessonGroup does not belong to the specified course", exception.getMessage());

    verify(lessonGroupRepository, times(1)).findLessonGroupById(1);
    verify(lessonGroupDomainService, never()).updateLessonGroup(any(), any());
  }

  @Test
  void 正常系_レッスングループを作成できること() {
    // Arrange
    LessonGroupCreateCommand command = LessonGroupCreateCommand.create(100, "新しいグループ");
    LessonGroup createdLessonGroup = new LessonGroup(
        2,
        100,
        new com.everrefine.elms.domain.model.Order(new BigDecimal("2")),
        new com.everrefine.elms.domain.model.lesson.Title("新しいグループ"),
        LocalDateTime.now(),
        LocalDateTime.now()
    );
    
    when(lessonGroupDomainService.issueLessonGroupOrder(100))
        .thenReturn(new BigDecimal("2"));
    when(lessonGroupRepository.createLessonGroup(any(LessonGroup.class)))
        .thenReturn(createdLessonGroup);

    // Act
    LessonGroupDto result = lessonGroupApplicationService.createLessonGroup(command);

    // Assert
    assertEquals(2, result.id());
    assertEquals(100, result.courseId());
    assertEquals(new BigDecimal("2"), result.lessonGroupOrder());
    assertEquals("新しいグループ", result.name());

    verify(lessonGroupDomainService, times(1)).issueLessonGroupOrder(100);
    verify(lessonGroupRepository, times(1)).createLessonGroup(any(LessonGroup.class));
  }

  @Test
  void 正常系_レッスングループを削除できること() {
    // Arrange
    when(lessonGroupRepository.findLessonGroupById(1))
        .thenReturn(Optional.of(existingLessonGroup));

    // Act
    lessonGroupApplicationService.deleteLessonGroupById(1);

    // Assert
    verify(lessonGroupRepository, times(1)).findLessonGroupById(1);
    verify(lessonGroupRepository, times(1)).deleteLessonGroupById(1);
  }

  @Test
  void 正常系_存在しないレッスングループを削除してもエラーにならないこと() {
    // Arrange
    when(lessonGroupRepository.findLessonGroupById(999))
        .thenReturn(Optional.empty());

    // Act
    lessonGroupApplicationService.deleteLessonGroupById(999);

    // Assert
    verify(lessonGroupRepository, times(1)).findLessonGroupById(999);
    verify(lessonGroupRepository, never()).deleteLessonGroupById(999);
  }
}
