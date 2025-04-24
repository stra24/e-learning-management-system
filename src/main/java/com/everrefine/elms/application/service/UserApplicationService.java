package com.everrefine.elms.application.service;

import com.everrefine.elms.application.dto.UserDto;
import com.everrefine.elms.application.dto.UserPageDto;

public interface UserApplicationService {

  UserDto findUserById(String userId);

  UserPageDto findUsers(int pageNum, int pageSize);
}
