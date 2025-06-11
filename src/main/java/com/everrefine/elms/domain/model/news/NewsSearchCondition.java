package com.everrefine.elms.domain.model.news;

import com.everrefine.elms.domain.model.PagerForRequest;
import java.time.LocalDate;
import lombok.Value;

@Value
public class NewsSearchCondition {

  /**
   * リクエスト用のページャー情報
   */
  PagerForRequest pagerForRequest;

  /**
   * お知らせのタイトル
   */
  String title;

  /**
   * 検索条件：開始日
   */
  LocalDate createdDateFrom;

  /**
   * 検索条件：終了日
   */
  LocalDate createDateTo;

  public NewsSearchCondition(
      int pageNum,
      int pageSize,
      String title,
      LocalDate createdDateFrom,
      LocalDate createDateTo
  ) {
    this.pagerForRequest = new PagerForRequest(pageNum, pageSize);
    this.title = title;
    this.createdDateFrom = createdDateFrom;
    this.createDateTo = createDateTo;
  }

}
