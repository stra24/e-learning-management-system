package com.everrefine.elms.domain.model.user;

import com.everrefine.elms.domain.model.PagerForRequest;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Value;

@Value
public class UserSearchCondition {

  /**
   * リクエスト用のページャー情報
   */
  @NotNull
  PagerForRequest pagerForRequest;

  /**
   * ユーザーID
   */
  @Nullable
  String userId;

  /**
   * 権限
   */
  @Nullable
  String userRole;

  /**
   * 氏名
   */
  @Nullable
  String realName;

  /**
   * ユーザー名
   */
  @Nullable
  String userName;

  /**
   * メールアドレス
   */
  @Nullable
  String emailAddress;

  /**
   * 作成日From
   */
  @Nullable
  LocalDate createdDateFrom;

  /**
   * 作成日To
   */
  @Nullable
  LocalDate createdDateTo;

  public UserSearchCondition(
      int pageNum,
      int pageSize,
      String userId,
      String userRole,
      String realName,
      String userName,
      String emailAddress,
      LocalDate createdDateFrom,
      LocalDate createdDateTo
  ) {
    this.pagerForRequest = new PagerForRequest(pageNum, pageSize);
    this.userId = userId;
    this.userRole = userRole;
    this.realName = realName;
    this.userName = userName;
    this.emailAddress = emailAddress;
    this.createdDateFrom = createdDateFrom;
    this.createdDateTo = createdDateTo;
  }

  public int getPageSize() {
    return pagerForRequest.getPageSize();
  }

  public int getOffset() {
    return pagerForRequest.getOffset();
  }
}