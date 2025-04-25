package com.everrefine.elms.application.dto;

import com.everrefine.elms.domain.model.news.News;
import com.everrefine.elms.domain.model.pager.PagerResponse;
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

  public NewsPageDto(List<News> news, PagerResponse pagerResponse) {
    newsDtos = news.stream().map(NewsDto::new).toList();
    pageNum = pagerResponse.getPageNum();
    pageSize = pagerResponse.getPageSize();
    totalSize = pagerResponse.getTotalSize();
  }
}