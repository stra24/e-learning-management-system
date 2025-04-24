package com.everrefine.elms.domain.model.pager;

import lombok.Value;

/**
 * データ取得のリクエスト時に使うページャーの値オブジェクト。
 */
@Value
public class PagerRequest {

  /**
   * ページ番号
   */
  int pageNum;

  /**
   * 1ページ当たりの件数
   */
  int pageSize;

  public PagerRequest(int pageNum, int pageSize) {
    if (pageNum < 1) {
      throw new IllegalArgumentException("pageNum must be at least 1");
    }
    if (pageSize < 1) {
      throw new IllegalArgumentException("pageSize must be at least 1");
    }
    this.pageNum = pageNum;
    this.pageSize = pageSize;
  }

  public int getOffset() {
    return (pageNum - 1) * pageSize;
  }
}