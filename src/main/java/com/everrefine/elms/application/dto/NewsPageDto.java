package com.everrefine.elms.application.dto;

import com.everrefine.elms.domain.model.news.News;
import com.everrefine.elms.domain.model.pager.PagerForResponse;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;

@Getter
public class NewsPageDto {

  /**
   * お知らせDTOリスト
   */
  private final List<NewsDto> newsDtos;
  /**
   * ページ番号
   */
  private final int pageNum;

  /**
   * 1ページ当たりの件数
   */
  private final int pageSize;

  /**
   * 総データ件数
   */
  private final int totalSize;

  /**
   * 検索条件のお知らせタイトル
   */
  private final String title;

  /**
   * 検索条件の開始日
   */
  private final LocalDate createdDateFrom;

  /**
   * 検索条件の終了日
   */
  private final LocalDate createDateTo;

  //不要になったら削除する！！
  public NewsPageDto(List<News> news, PagerForResponse pagerForResponse) {
    newsDtos = news.stream().map(NewsDto::new).toList();
    pageNum = pagerForResponse.getPageNum();
    pageSize = pagerForResponse.getPageSize();
    totalSize = pagerForResponse.getTotalSize();
    title = pagerForResponse.getTitle();
    createdDateFrom = pagerForResponse.getCreatedDateFrom();
    createDateTo = pagerForResponse.getCreateDateTo();
  }

  public NewsPageDto(List<News> news,
      int pageNum,
      int pageSize,
      int totalSize,
      String title,
      LocalDate createdDateFrom,
      LocalDate createDateTo) {
    newsDtos = news.stream().map(NewsDto::new).toList();
    this.pageNum = pageNum;
    this.pageSize = pageSize;
    this.totalSize = totalSize;
    this.title = title;
    this.createdDateFrom = createdDateFrom;
    this.createDateTo = createDateTo;
  }
}