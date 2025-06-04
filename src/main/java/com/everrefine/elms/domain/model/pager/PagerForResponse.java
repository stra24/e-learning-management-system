package com.everrefine.elms.domain.model.pager;

import lombok.Value;

/**
 * ページネーション結果の値オブジェクト。
 */
@Value
public class PagerForResponse {

  /**
   * ページ番号
   */
  int pageNum;

  /**
   * 1ページ当たりの件数
   */
  int pageSize;

  /**
   * 総データ件数
   */
  int totalSize;

  /**
   * 検索条件のお知らせタイトル
   */
  String title;

  /**
   * 検索条件の開始日
   */

  /**
   * 検索条件の終了日
   */

  public PagerForResponse(int pageNum, int pageSize, int totalSize, String title) {
    if (pageNum < 1) {
      throw new IllegalArgumentException("pageNum must be at least 1");
    }
    if (pageSize < 1) {
      throw new IllegalArgumentException("pageSize must be at least 1");
    }
    if (totalSize < 0) {
      throw new IllegalArgumentException("totalSize must not be negative");
    }
    this.pageNum = pageNum;
    this.pageSize = pageSize;
    this.totalSize = totalSize;
    this.title = title;
  }

  /**
   * 総ページ数（最後のページ番号）
   */
  public int getTotalPages() {
    return (int) Math.ceil((double) totalSize / pageSize);
  }

  /**
   * 次ページが存在するか
   */
  public boolean hasNextPage() {
    return pageNum < getTotalPages();
  }

  /**
   * 前ページが存在するか
   */
  public boolean hasPreviousPage() {
    return pageNum > 1;
  }
}