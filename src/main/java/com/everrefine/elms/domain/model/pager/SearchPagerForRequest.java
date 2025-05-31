package com.everrefine.elms.domain.model.pager;

import java.time.LocalDate;
import lombok.Value;

@Value
public class SearchPagerForRequest {

  /**
   * ページ番号
   */
  int pageNum;

  /**
   * 1ページ当たりの件数
   */
  int pageSize;

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

  /**
   * 層データ数
   */
  int totalSize;


  public SearchPagerForRequest(
      int pageNum,
      int pageSize,
      String title,
      LocalDate createdDateFrom,
      LocalDate createDateTo,
      int totalSize) {
    if (pageNum < 1) {
      throw new IllegalArgumentException("pageNum must be at least 1");
    }
    if (pageSize < 1) {
      throw new IllegalArgumentException("pageSize must be at least 1");
    }
    if (createdDateFrom.isAfter(createDateTo)) {
      throw new IllegalArgumentException("createdDateFrom must be earlier than createDateTo");
    }
    this.pageNum = pageNum;
    this.pageSize = pageSize;
    this.title = title;
    this.createdDateFrom = createdDateFrom;
    this.createDateTo = createDateTo;
    this.totalSize = totalSize;
  }
}
