package com.everrefine.elms.application.service;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.everrefine.elms.domain.model.lesson.Lesson;
import com.everrefine.elms.domain.repository.LessonRepository;
import com.everrefine.elms.domain.service.LessonDomainService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * {@link LessonApplicationServiceImpl#deleteLessonById(Integer)} のユニットテスト。
 *
 * <p>設計メモ「Lesson DELETE API 設計メモ」のテスト戦略(7-1)に基づき、Repository をモックして
 * 以下の振る舞いを検証する。
 * <ul>
 *   <li>存在するレッスンIDを指定した場合: findById と deleteLessonById が呼ばれること</li>
 *   <li>存在しないレッスンIDを指定した場合: findById は呼ばれるが deleteLessonById は呼ばれないこと</li>
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
class LessonApplicationServiceImplTest {

  @Mock
  private LessonRepository lessonRepository;

  @Mock
  private LessonDomainService lessonDomainService;

  @InjectMocks
  private LessonApplicationServiceImpl lessonApplicationService;

  @Test
  void 正常系_存在するレッスンIDを指定した場合は削除処理が実行されること() {
    // Arrange
    Integer lessonId = 1;
    Lesson lesson = mock(Lesson.class);
    when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));

    // Act
    lessonApplicationService.deleteLessonById(lessonId);

    // Assert
    verify(lessonRepository).findById(lessonId);
    verify(lessonRepository).deleteLessonById(lessonId);
  }

  @Test
  void 正常系_存在しないレッスンIDを指定した場合は削除処理が実行されないこと() {
    // Arrange
    Integer lessonId = 1;
    when(lessonRepository.findById(lessonId)).thenReturn(Optional.empty());

    // Act
    lessonApplicationService.deleteLessonById(lessonId);

    // Assert
    verify(lessonRepository).findById(lessonId);
    verify(lessonRepository, never()).deleteLessonById(any());
  }
}
