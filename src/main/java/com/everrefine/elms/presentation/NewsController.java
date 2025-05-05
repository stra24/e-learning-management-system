package com.everrefine.elms.presentation;

import com.everrefine.elms.application.command.NewsCreateCommand;
import com.everrefine.elms.application.dto.NewsDto;
import com.everrefine.elms.application.dto.NewsPageDto;
import com.everrefine.elms.application.service.NewsApplicationService;
import com.everrefine.elms.presentation.request.NewsCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {

  private final NewsApplicationService newsApplicationService;

/**
 * 指定したお知らせを更新する。
 *
 */




  /**
   * お知らせを新規登録する
   *
   * @param newsCreateRequest newsの新規作成リクエスト（リクエストボディ）
   */

  @PostMapping()
  public ResponseEntity<Void> createNews(@RequestBody NewsCreateRequest newsCreateRequest) {
    NewsCreateCommand newsCreateCommand = NewsCreateCommand.create(
        newsCreateRequest.getTitle(),
        newsCreateRequest.getContent()
    );
    newsApplicationService.createNews(newsCreateCommand);
    return ResponseEntity.ok().build();
  }

  /**
   * 指定したユーザーIDのユーザーを削除する
   *
   * @param newsId newsID(UUID形式の文字列）
   */
  @DeleteMapping("/{newsId}")
  public ResponseEntity<Void> deleteNewsById(@PathVariable String newsId) {
    newsApplicationService.deleteNewsById(newsId);
    return ResponseEntity.noContent().build();
  }



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
