package com.everrefine.elms.presentation;

import com.everrefine.elms.application.command.LessonCreateCommand;
import com.everrefine.elms.application.command.LessonUpdateCommand;
import com.everrefine.elms.application.dto.CourseLessonsDto;
import com.everrefine.elms.application.dto.FirstLessonDto;
import com.everrefine.elms.application.dto.LessonDto;
import com.everrefine.elms.application.service.LessonApplicationService;
import com.everrefine.elms.presentation.request.LessonCreateRequest;
import com.everrefine.elms.presentation.request.LessonOrderUpdateRequest;
import com.everrefine.elms.presentation.request.LessonUpdateRequest;
import jakarta.validation.Valid;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

  private final LessonApplicationService lessonApplicationService;

  private static final long ORDER_RATE_WINDOW_MILLIS = 10_000L;
  private static final int ORDER_RATE_LIMIT = 30;
  private static final ConcurrentHashMap<Integer, OrderRateState> orderRateStateByUserId = new ConcurrentHashMap<>();

  private static final class OrderRateState {
    private long windowStartMillis;
    private int count;

    private OrderRateState(long windowStartMillis, int count) {
      this.windowStartMillis = windowStartMillis;
      this.count = count;
    }
  }

  private void assertAdmin(Authentication authentication) {
    if (authentication == null) {
      throw new AccessDeniedException("Permission denied");
    }

    boolean isAdmin = authentication.getAuthorities().stream()
        .anyMatch(a -> "ADMIN".equals(a.getAuthority()));
    if (!isAdmin) {
      throw new AccessDeniedException("Permission denied");
    }
  }

  private void assertNotRateLimited(Integer userId) {
    long now = System.currentTimeMillis();
    OrderRateState state = orderRateStateByUserId.computeIfAbsent(userId,
        id -> new OrderRateState(now, 0));

    synchronized (state) {
      if (now - state.windowStartMillis >= ORDER_RATE_WINDOW_MILLIS) {
        state.windowStartMillis = now;
        state.count = 0;
      }
      state.count++;
      if (state.count > ORDER_RATE_LIMIT) {
        throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests");
      }
    }
  }

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
      @RequestBody @Valid LessonCreateRequest lessonCreateRequest,
      Authentication authentication
  ) {
    assertAdmin(authentication);
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
      @RequestBody @Valid LessonUpdateRequest lessonUpdateRequest,
      Authentication authentication
  ) {
    assertAdmin(authentication);
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
      @PathVariable Integer lessonId,
      Authentication authentication
  ) {
    assertAdmin(authentication);
    lessonApplicationService.deleteLessonById(lessonId);
    return ResponseEntity.noContent().build();
  }

  /**
   * レッスンの並び順を更新する。
   *
   * @param courseId                  コースID
   * @param lessonGroupId             レッスングループID
   * @param lessonId                  レッスンID
   * @param lessonOrderUpdateRequest  レッスン並び順更新リクエスト
   * @return 更新されたレッスンDTO
   */
  @PatchMapping("/lesson-groups/{lessonGroupId}/lessons/{lessonId}/order")
  public ResponseEntity<LessonDto> updateLessonOrder(
      @PathVariable Integer courseId,
      @PathVariable Integer lessonGroupId,
      @PathVariable Integer lessonId,
      @RequestBody @Valid LessonOrderUpdateRequest lessonOrderUpdateRequest,
      Authentication authentication
  ) {
    assertAdmin(authentication);
    Integer operatorUserId = Integer.valueOf(authentication.getName());
    assertNotRateLimited(operatorUserId);
    LessonDto updatedLessonDto = lessonApplicationService.updateLessonOrder(
        operatorUserId,
        lessonId,
        lessonOrderUpdateRequest.getPrecedingLessonId(),
        lessonOrderUpdateRequest.getFollowingLessonId()
    );
    return ResponseEntity.ok(updatedLessonDto);
  }
}
