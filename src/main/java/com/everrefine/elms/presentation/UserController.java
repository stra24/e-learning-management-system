package com.everrefine.elms.presentation;

import com.everrefine.elms.application.command.UserCreateCommand;
import com.everrefine.elms.application.command.UserUpdateCommand;
import com.everrefine.elms.application.dto.UserDto;
import com.everrefine.elms.application.dto.UserPageDto;
import com.everrefine.elms.application.service.UserApplicationService;
import com.everrefine.elms.presentation.request.UserCreateRequest;
import com.everrefine.elms.presentation.request.UserUpdateRequest;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserApplicationService userApplicationService;

  /**
   * 指定したユーザーを更新する。
   *
   * @param userId            ユーザーID（パスパラメータ）
   * @param userUpdateRequest ユーザーの更新リクエスト（リクエストボディ）
   */
  @PutMapping("/{userId}")
  public ResponseEntity<Void> updateUser(
      @PathVariable String userId,
      @RequestBody UserUpdateRequest userUpdateRequest
  ) {
    UserUpdateCommand userUpdateCommand = UserUpdateCommand.create(
        UUID.fromString(userId),
        userUpdateRequest.getRealName(),
        userUpdateRequest.getUserName(),
        userUpdateRequest.getEmailAddress(),
        userUpdateRequest.getThumbnailUrl()
    );
    userApplicationService.updateUser(userUpdateCommand);
    return ResponseEntity.ok().build();
  }

  /**
   * ユーザーを新規作成する。
   *
   * @param userCreateRequest ユーザーの新規作成リクエスト（リクエストボディ）
   */
  @PostMapping()
  public ResponseEntity<Void> createUser(@RequestBody UserCreateRequest userCreateRequest) {
    UserCreateCommand userCreateCommand = UserCreateCommand.create(
        userCreateRequest.getRealName(),
        userCreateRequest.getUserName(),
        userCreateRequest.getEmailAddress(),
        userCreateRequest.getPassword(),
        userCreateRequest.getConfirmPassword(),
        userCreateRequest.getThumbnailUrl(),
        userCreateRequest.getUserRole()
    );
    userApplicationService.createUser(userCreateCommand);
    return ResponseEntity.ok().build();
  }

  /**
   * 指定したユーザーIDのユーザーを削除する。
   *
   * @param userId ユーザーID（UUID形式の文字列）
   */
  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> deleteUserById(@PathVariable String userId) {
    userApplicationService.deleteUserById(userId);
    return ResponseEntity.noContent().build();
  }

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
