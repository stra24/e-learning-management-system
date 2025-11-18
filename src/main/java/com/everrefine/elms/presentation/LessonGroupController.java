package com.everrefine.elms.presentation;

import com.everrefine.elms.application.command.LessonGroupCreateCommand;
import com.everrefine.elms.application.dto.LessonGroupDto;
import com.everrefine.elms.application.service.LessonGroupApplicationService;
import com.everrefine.elms.presentation.request.LessonGroupCreateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/courses/{courseId}")
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
  @PostMapping("/lesson-groups")
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
}
