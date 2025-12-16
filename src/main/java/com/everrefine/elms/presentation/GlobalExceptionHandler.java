package com.everrefine.elms.presentation;

import com.everrefine.elms.application.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * グローバル例外ハンドラー。
 * 
 * <p>アプリケーション全体で発生する例外をハンドリングし、
 * 適切なHTTPステータスコードとエラーメッセージを返却します。
 */
@ControllerAdvice
public class GlobalExceptionHandler {

  /**
   * リソースが見つからない場合の例外ハンドラー。
   *
   * @param e ResourceNotFoundException
   * @return 404 Not Found
   */
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
  }
}
