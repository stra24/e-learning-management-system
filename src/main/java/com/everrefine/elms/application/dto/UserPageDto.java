package com.everrefine.elms.application.dto;

import com.everrefine.elms.domain.model.pager.PagerForResponse;
import com.everrefine.elms.domain.model.user.User;
import java.util.List;
import lombok.Getter;

@Getter
public class UserPageDto {

  /**
   * ユーザーDTOリスト
   */
  private final List<UserDto> userDtos;
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

  public UserPageDto(List<User> users, PagerForResponse pagerForResponse) {
    userDtos = users.stream().map(UserDto::new).toList();
    pageNum = pagerForResponse.getPageNum();
    pageSize = pagerForResponse.getPageSize();
    totalSize = pagerForResponse.getTotalSize();
  }
}