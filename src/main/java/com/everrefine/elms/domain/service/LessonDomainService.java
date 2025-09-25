package com.everrefine.elms.domain.service;

import java.math.BigDecimal;

public interface LessonDomainService {
  
  /**
   * レッスンの並び順を発番する。
   * 指定されたレッスングループ内で最大のlesson_order + 1を返す。
   * レッスンが存在しない場合は1を返す。
   *
   * @param lessonGroupId レッスングループID
   * @return 発番されたレッスンの並び順
   */
  BigDecimal issueLessonOrder(Integer lessonGroupId);
}
