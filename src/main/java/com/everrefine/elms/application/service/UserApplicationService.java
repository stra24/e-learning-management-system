package com.everrefine.elms.application.service;

import com.everrefine.elms.application.command.UserCreateCommand;
import com.everrefine.elms.application.command.UserSearchCommand;
import com.everrefine.elms.application.command.UserUpdateCommand;
import com.everrefine.elms.application.dto.UserDto;
import com.everrefine.elms.application.dto.UserPageDto;

public interface UserApplicationService {
  UserDto findUserById(Integer userId);

  UserPageDto findUsers(UserSearchCommand userSearchCommand);

  void createUser(UserCreateCommand userCreateCommand);

  void updateUser(UserUpdateCommand userUpdateCommand);

  void deleteUserById(Integer userId);
}
