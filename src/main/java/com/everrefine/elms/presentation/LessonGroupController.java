package com.everrefine.elms.presentation;

import com.everrefine.elms.application.command.LessonGroupCreateCommand;
import com.everrefine.elms.application.command.LessonGroupUpdateCommand;
import com.everrefine.elms.application.dto.LessonGroupDto;
import com.everrefine.elms.application.service.LessonGroupApplicationService;
import com.everrefine.elms.presentation.request.LessonGroupCreateRequest;
import com.everrefine.elms.presentation.request.LessonGroupUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/courses/{courseId}/lesson-groups")
@RequiredArgsConstructor
public class LessonGroupController {

  private final LessonGroupApplicationService lessonGroupApplicationService;

  /**
   * レッスングループを新規作成する。
   *
   * @param courseId                 コースID
   * @param lessonGroupCreateRequest レッスングループ作成リクエスト
   * @return 作成されたレッスンDTO
   */
  @PostMapping
  public ResponseEntity<LessonGroupDto> createLessonGroup(
      @PathVariable Integer courseId,
      @RequestBody @Valid LessonGroupCreateRequest lessonGroupCreateRequest
  ) {
    LessonGroupCreateCommand lessonGroupCreateCommand = LessonGroupCreateCommand.create(
        courseId,
        lessonGroupCreateRequest.getTitle()
    );

    LessonGroupDto createdLessonGroupDto = lessonGroupApplicationService.createLessonGroup(
        lessonGroupCreateCommand);
    return ResponseEntity.ok(createdLessonGroupDto);
  }

  /**
   * レッスングループを更新する。
   *
   * @param courseId                 コースID
   * @param lessonGroupId            レッスングループID
   * @param lessonGroupUpdateRequest レッスングループ更新リクエスト
   * @return 更新されたレッスングループDTO
   */
  @PutMapping("/{lessonGroupId}")
  public ResponseEntity<LessonGroupDto> updateLessonGroup(
      @PathVariable Integer courseId,
      @PathVariable Integer lessonGroupId,
      @RequestBody @Valid LessonGroupUpdateRequest lessonGroupUpdateRequest
  ) {
    LessonGroupUpdateCommand lessonGroupUpdateCommand = LessonGroupUpdateCommand.create(
        lessonGroupId,
        lessonGroupUpdateRequest.getTitle()
    );

    LessonGroupDto updatedLessonGroupDto = lessonGroupApplicationService.updateLessonGroup(
        lessonGroupUpdateCommand);
    return ResponseEntity.ok(updatedLessonGroupDto);
  }

  /**
   * 指定したレッスングループを削除する。
   *
   * @param courseId      コースID
   * @param lessonGroupId レッスングループID
   */
  @DeleteMapping("/{lessonGroupId}")
  public ResponseEntity<Void> deleteLessonGroup(
      @PathVariable Integer courseId,
      @PathVariable Integer lessonGroupId
  ) {
    lessonGroupApplicationService.deleteLessonGroupById(lessonGroupId);
    return ResponseEntity.noContent().build();
  }
}
