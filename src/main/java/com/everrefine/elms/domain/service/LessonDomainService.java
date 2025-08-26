package com.everrefine.elms.domain.service;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class LessonDomainService {

  /**
   * レッスンが有効かどうかをチェックします。
   * 
   * @param lessonId レッスンID
   * @return 有効な場合はtrue、そうでなければfalse
   */
  public static boolean isValidLesson(UUID lessonId) {
    return lessonId != null;
  }

  /**
   * コースIDが有効かどうかをチェックします。
   * 
   * @param courseId コースID
   * @return 有効な場合はtrue、そうでなければfalse
   */
  public static boolean isValidCourseId(UUID courseId) {
    return courseId != null;
  }
}
