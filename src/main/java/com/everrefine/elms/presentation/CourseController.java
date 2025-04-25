package com.everrefine.elms.presentation;

import com.everrefine.elms.application.dto.CourseDto;
import com.everrefine.elms.application.dto.CoursePageDto;
import com.everrefine.elms.application.service.CourseApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

  private final CourseApplicationService courseApplicationService;

  /**
   * 指定したコースIDでコースを取得する。
   *
   * @param courseId コースID（UUID形式の文字列）
   * @return コースDTO
   */
  @GetMapping("/{courseId}")
  public ResponseEntity<CourseDto> findCourseById(@PathVariable String courseId) {
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
  @GetMapping()
  public ResponseEntity<CoursePageDto> findCourses(
      @RequestParam(defaultValue = "1") int pageNum,
      @RequestParam(defaultValue = "10") int pageSize
  ) {
    CoursePageDto coursePageDto = courseApplicationService.findCourses(pageNum, pageSize);
    return ResponseEntity.ok(coursePageDto);
  }
}
