package com.everrefine.elms.presentation;

import com.everrefine.elms.application.dto.NewsDto;
import com.everrefine.elms.application.dto.NewsPageDto;
import com.everrefine.elms.application.service.NewsApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {

  private final NewsApplicationService newsApplicationService;

  /**
   * 指定したお知らせIDでお知らせを取得する。
   *
   * @param newsId お知らせID（UUID形式の文字列）
   * @return お知らせDTO
   */
  @GetMapping("/{newsId}")
  public ResponseEntity<NewsDto> findNewsById(@PathVariable String newsId) {
    NewsDto newsDto = newsApplicationService.findNewsById(newsId);
    return ResponseEntity.ok(newsDto);
  }

  /**
   * 指定した範囲の全てのお知らせを取得する。
   *
   * @param pageNum  ページ番号
   * @param pageSize 1ページ当たりの件数
   * @return お知らせのページ情報を表すDTO
   */
  @GetMapping()
  public ResponseEntity<NewsPageDto> findNews(
      @RequestParam(defaultValue = "1") int pageNum,
      @RequestParam(defaultValue = "10") int pageSize
  ) {
    NewsPageDto newsPageDto = newsApplicationService.findNews(pageNum, pageSize);
    return ResponseEntity.ok(newsPageDto);
  }
}
