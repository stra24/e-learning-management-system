package com.everrefine.elms.presentation;

import com.everrefine.elms.application.command.CourseCreateCommand;
import com.everrefine.elms.application.command.CourseUpdateCommand;
import com.everrefine.elms.application.dto.CourseDto;
import com.everrefine.elms.application.dto.CoursePageDto;
import com.everrefine.elms.application.service.CourseApplicationService;
import com.everrefine.elms.presentation.request.CourseCreateRequest;
import com.everrefine.elms.presentation.request.CourseUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

  private final CourseApplicationService courseApplicationService;

  /**
   * 指定したコースを更新する。
   *
   * @param courseId            コースID（パスパラメータ）
   * @param courseUpdateRequest コースの更新リクエスト（リクエストボディ）
   */
  @PutMapping("/{courseId}")
  public ResponseEntity<Void> updateCourse(
      @PathVariable Integer courseId,
      @RequestBody CourseUpdateRequest courseUpdateRequest
  ) {
    CourseUpdateCommand courseUpdateCommand = CourseUpdateCommand.create(
        courseId,
        courseUpdateRequest.getCourseOrder(),
        courseUpdateRequest.getTitle(),
        courseUpdateRequest.getDescription(),
        courseUpdateRequest.getThumbnailUrl()
    );
    courseApplicationService.updateCourse(courseUpdateCommand);
    return ResponseEntity.ok().build();
  }

  /**
   * コースを新規作成する。
   *
   * @param courseCreateRequest コースの新規作成リクエスト（リクエストボディ）
   */
  @PostMapping
  public ResponseEntity<Void> createCourse(@RequestBody CourseCreateRequest courseCreateRequest) {
    CourseCreateCommand courseCreateCommand = CourseCreateCommand.create(
        courseCreateRequest.getThumbnailUrl(),
        courseCreateRequest.getTitle(),
        courseCreateRequest.getDescription()
    );
    courseApplicationService.createCourse(courseCreateCommand);
    return ResponseEntity.ok().build();
  }

  /**
   * 指定したコースIDのコースを削除する。
   *
   * @param courseId コースID（UUID形式の文字列）
   */
  @DeleteMapping("/{courseId}")
  public ResponseEntity<Void> deleteCourseById(@PathVariable Integer courseId) {
    courseApplicationService.deleteCourseById(courseId);
    return ResponseEntity.noContent().build();
  }

  /**
   * 指定したコースIDでコースを取得する。
   *
   * @param courseId コースID（UUID形式の文字列）
   * @return コースDTO
   */
  @GetMapping("/{courseId}")
  public ResponseEntity<CourseDto> findCourseById(@PathVariable Integer courseId) {
    CourseDto courseDto = courseApplicationService.findCourseById(courseId);
    return ResponseEntity.ok(courseDto);
  }

  /**
   * 指定した範囲の全てのコースを取得する。
   *
   * @param pageNum  ページ番号
   * @param pageSize 1ページ当たりの件数
   * @return コースのページ情報を表すDTO
   */
  @GetMapping
  public ResponseEntity<CoursePageDto> findCourses(
      @RequestParam(defaultValue = "1") int pageNum,
      @RequestParam(defaultValue = "10") int pageSize
  ) {
    CoursePageDto coursePageDto = courseApplicationService.findCourses(pageNum, pageSize);
    return ResponseEntity.ok(coursePageDto);
  }
}
