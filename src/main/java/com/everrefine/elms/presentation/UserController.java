package com.everrefine.elms.presentation;

import com.everrefine.elms.application.dto.UserDto;
import com.everrefine.elms.application.dto.UserPageDto;
import com.everrefine.elms.application.service.UserApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserApplicationService userApplicationService;

  /**
   * 指定したユーザーIDでユーザーを取得する。
   *
   * @param userId ユーザーID（UUID形式の文字列）
   * @return ユーザーDTO
   */
  @GetMapping("/{userId}")
  public ResponseEntity<UserDto> findUserById(@PathVariable String userId) {
    UserDto userDto = userApplicationService.findUserById(userId);
    return ResponseEntity.ok(userDto);
  }

  /**
   * 指定した範囲の全てのユーザーを取得する。
   *
   * @param pageNum  ページ番号
   * @param pageSize 1ページ当たりの件数
   * @return ユーザーのページ情報を表すDTO
   */
  @GetMapping()
  public ResponseEntity<UserPageDto> findUsers(
      @RequestParam(defaultValue = "1") int pageNum,
      @RequestParam(defaultValue = "10") int pageSize
  ) {
    UserPageDto userPageDto = userApplicationService.findUsers(pageNum, pageSize);
    return ResponseEntity.ok(userPageDto);
  }
}
