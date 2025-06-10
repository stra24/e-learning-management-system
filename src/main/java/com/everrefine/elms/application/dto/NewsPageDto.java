package com.everrefine.elms.application.dto;

import com.everrefine.elms.domain.model.news.News;
import com.everrefine.elms.domain.model.pager.PagerForResponse;
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


  //不要になったら削除する！！
  public NewsPageDto(List<News> news, PagerForResponse pagerForResponse) {
    newsDtos = news.stream().map(NewsDto::new).toList();
    pageNum = pagerForResponse.getPageNum();
    pageSize = pagerForResponse.getPageSize();
    totalSize = pagerForResponse.getTotalSize();
  }

  public NewsPageDto(List<NewsDto> newsDto,
      int pageNum,
      int pageSize,
      int totalSize) {
    this.newsDtos = newsDto;
    this.pageNum = pageNum;
    this.pageSize = pageSize;
    this.totalSize = totalSize;
  }
}