package com.everrefine.elms.application.exception;

/**
 * リソースが見つからない場合にスローされる例外。
 */
public class ResourceNotFoundException extends RuntimeException {

  public ResourceNotFoundException(String message) {
    super(message);
  }

  public ResourceNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
