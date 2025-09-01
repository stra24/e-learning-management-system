package com.everrefine.elms.presentation;

import com.everrefine.elms.application.dto.FirstLessonDto;
import com.everrefine.elms.application.dto.LessonDto;
import com.everrefine.elms.application.service.LessonApplicationService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/courses/{courseId}/lessons")
@RequiredArgsConstructor
public class LessonController {

  private final LessonApplicationService lessonApplicationService;

  /**
   * 該当コースの先頭のレッスンを取得する。
   *
   * @param courseId コースID
   * @return 該当コースの先頭のレッスン
   */
  @GetMapping("/first")
  public ResponseEntity<FirstLessonDto> findFirstLessonIdByCourseId(@PathVariable String courseId) {
    FirstLessonDto dto = lessonApplicationService.findFirstLessonIdByCourseId(courseId);
    return ResponseEntity.ok(dto);
  }

  /**
   * 指定したコースIDとレッスンIDでレッスンを取得する。
   *
   * @param courseId コースID
   * @param lessonId レッスンID
   * @return レッスンDTO
   */
  @GetMapping("/{lessonId}")
  public ResponseEntity<LessonDto> findLessonById(
      @PathVariable @NotBlank String courseId,
      @PathVariable @NotBlank String lessonId
  ) {
    LessonDto lessonDto = lessonApplicationService.findLessonById(courseId, lessonId);
    return ResponseEntity.ok(lessonDto);
  }
}
