package com.everrefine.elms.presentation;

import com.everrefine.elms.application.command.LessonCreateCommand;
import com.everrefine.elms.application.command.LessonUpdateCommand;
import com.everrefine.elms.application.dto.CourseLessonsDto;
import com.everrefine.elms.application.dto.FirstLessonDto;
import com.everrefine.elms.application.dto.LessonDto;
import com.everrefine.elms.application.service.LessonApplicationService;
import com.everrefine.elms.presentation.request.LessonCreateRequest;
import com.everrefine.elms.presentation.request.LessonUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/courses/{courseId}")
@RequiredArgsConstructor
public class LessonController {

  /**
   * レッスンアプリケーションサービス。
   */
  private final LessonApplicationService lessonApplicationService;

  /**
   * 該当コースの先頭のレッスンを取得する。
   *
   * @param courseId コースID
   * @return 該当コースの先頭のレッスン
   */
  @GetMapping("/lessons/first")
  public ResponseEntity<FirstLessonDto> findFirstLessonIdByCourseId(
      @PathVariable Integer courseId) {
    FirstLessonDto dto = lessonApplicationService.findFirstLessonIdByCourseId(courseId);
    return ResponseEntity.ok(dto);
  }

  /**
   * 指定したコースIDに紐づくレッスンをレッスングループ単位でグループ分けして取得する。
   *
   * @param courseId コースID
   * @return レッスングループ単位でグループ分けされたレッスン一覧
   */
  @GetMapping("/lessons")
  public ResponseEntity<CourseLessonsDto> findLessonsGroupedByLessonGroup(
      @PathVariable Integer courseId
  ) {
    CourseLessonsDto courseLessonsDto = lessonApplicationService.findLessonsGroupedByLessonGroup(
        courseId);
    return ResponseEntity.ok(courseLessonsDto);
  }

  /**
   * レッスンを新規作成する。
   *
   * @param courseId            コースID
   * @param lessonGroupId       レッスングループID
   * @param lessonCreateRequest レッスン作成リクエスト
   * @return 作成されたレッスンDTO
   */
  @PostMapping("/lesson-groups/{lessonGroupId}/lessons")
  public ResponseEntity<LessonDto> createLesson(
      @PathVariable Integer courseId,
      @PathVariable Integer lessonGroupId,
      @RequestBody @Valid LessonCreateRequest lessonCreateRequest
  ) {
    LessonCreateCommand lessonCreateCommand = LessonCreateCommand.create(
        courseId,
        lessonGroupId,
        lessonCreateRequest.getTitle(),
        lessonCreateRequest.getDescription(),
        lessonCreateRequest.getVideoUrl()
    );

    LessonDto createdLessonDto = lessonApplicationService.createLesson(lessonCreateCommand);
    return ResponseEntity.ok(createdLessonDto);
  }

  /**
   * 指定したコースIDとレッスンIDでレッスンを取得する。
   *
   * @param courseId      コースID
   * @param lessonGroupId レッスングループID
   * @param lessonId      レッスンID
   * @return レッスンDTO
   */
  @GetMapping("/lesson-groups/{lessonGroupId}/lessons/{lessonId}")
  public ResponseEntity<LessonDto> findLessonById(
      @PathVariable Integer courseId,
      @PathVariable Integer lessonGroupId,
      @PathVariable Integer lessonId
  ) {
    LessonDto lessonDto = lessonApplicationService.findLessonById(courseId, lessonId);
    return ResponseEntity.ok(lessonDto);
  }

  /**
   * レッスンを更新する。
   *
   * @param courseId            コースID
   * @param lessonGroupId       レッスングループID
   * @param lessonId            レッスンID
   * @param lessonUpdateRequest レッスン更新リクエスト
   * @return 更新されたレッスンDTO
   */
  @PutMapping("/lesson-groups/{lessonGroupId}/lessons/{lessonId}")
  public ResponseEntity<LessonDto> updateLesson(
      @PathVariable Integer courseId,
      @PathVariable Integer lessonGroupId,
      @PathVariable Integer lessonId,
      @RequestBody @Valid LessonUpdateRequest lessonUpdateRequest
  ) {
    LessonUpdateCommand lessonUpdateCommand = LessonUpdateCommand.create(
        lessonId,
        lessonUpdateRequest.getTitle(),
        lessonUpdateRequest.getDescription(),
        lessonUpdateRequest.getVideoUrl()
    );

    LessonDto updatedLessonDto = lessonApplicationService.updateLesson(lessonUpdateCommand);
    return ResponseEntity.ok(updatedLessonDto);
  }

  /**
   * レッスンを削除する。
   *
   * @param courseId      コースID
   * @param lessonGroupId レッスングループID
   * @param lessonId      レッスンID
   */
  @DeleteMapping("/lesson-groups/{lessonGroupId}/lessons/{lessonId}")
  public ResponseEntity<Void> deleteLessonById(
      @PathVariable Integer courseId,
      @PathVariable Integer lessonGroupId,
      @PathVariable Integer lessonId
  ) {
    lessonApplicationService.deleteLessonById(lessonId);
    return ResponseEntity.noContent().build();
  }
}
