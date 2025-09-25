package com.everrefine.elms.application.service;

import com.everrefine.elms.application.command.UserCreateCommand;
import com.everrefine.elms.application.command.UserSearchCommand;
import com.everrefine.elms.application.command.UserUpdateCommand;
import com.everrefine.elms.application.dto.UserDto;
import com.everrefine.elms.application.dto.UserPageDto;
import com.everrefine.elms.application.dto.converter.UserDtoConverter;
import com.everrefine.elms.domain.model.Url;
import com.everrefine.elms.domain.model.user.EmailAddress;
import com.everrefine.elms.domain.model.user.Password;
import com.everrefine.elms.domain.model.user.RealName;
import com.everrefine.elms.domain.model.user.User;
import com.everrefine.elms.domain.model.user.UserForUpdateRequest;
import com.everrefine.elms.domain.model.user.UserName;
import com.everrefine.elms.domain.model.user.UserSearchCondition;
import com.everrefine.elms.domain.repository.UserRepository;
import com.everrefine.elms.domain.service.UserDomainService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserApplicationServiceImpl implements UserApplicationService {

  private final UserRepository userRepository;

  @Override
  public UserDto findUserById(Integer userId) {
    User user = userRepository.findUserById(userId)
        .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    return UserDtoConverter.toDto(user);
  }

  @Override
  public UserPageDto findUsers(UserSearchCommand userSearchCommand) {
    UserSearchCondition userSearchCondition = new UserSearchCondition(
        userSearchCommand.getPageNum(),
        userSearchCommand.getPageSize(),
        userSearchCommand.getUserId(),
        userSearchCommand.getUserRole(),
        userSearchCommand.getRealName(),
        userSearchCommand.getUserName(),
        userSearchCommand.getEmailAddress(),
        userSearchCommand.getCreatedDateFrom(),
        userSearchCommand.getCreatedDateTo()
    );
    List<Integer> userIds = userRepository.findUserIdsBySearchConditions(userSearchCondition);
    List<User> users = userRepository.findUsersByIds(userIds);
    List<UserDto> userDtos = users.stream()
        .map(UserDtoConverter::toDto)
        .toList();
    int totalSize = userRepository.countUsers(userSearchCondition);
    return new UserPageDto(
        userDtos,
        userSearchCondition.getPagerForRequest().getPageNum(),
        userSearchCondition.getPagerForRequest().getPageSize(),
        totalSize
    );
  }

  @Override
  public void createUser(UserCreateCommand userCreateCommand) {
    if (!UserDomainService.matchesPassword(userCreateCommand.getPassword(),
        userCreateCommand.getConfirmPassword())) {
      throw new IllegalArgumentException("Password confirmation does not match");
    }

    User user = User.create(
        userCreateCommand.getEmailAddress(),
        userCreateCommand.getPassword(),
        userCreateCommand.getRealName(),
        userCreateCommand.getUserName(),
        userCreateCommand.getThumbnailUrl(),
        userCreateCommand.getUserRole()
    );

    userRepository.createUser(user);
  }

  @Override
  public void updateUser(UserUpdateCommand userUpdateCommand) {
    User user = userRepository.findUserById(userUpdateCommand.getId())
        .orElseThrow(() -> new RuntimeException("User not found with ID: " + userUpdateCommand.getId()));
    User updatedUser = user.update(
        userUpdateCommand.getEmailAddress(),
        null,
        userUpdateCommand.getRealName(),
        userUpdateCommand.getUserName(),
        userUpdateCommand.getThumbnailUrl(),
        null
    );
    userRepository.updateUser(updatedUser);
  }

  @Override
  public void deleteUserById(Integer userId) {
    // ユーザーが存在しなくてもエラーにはしない。
    userRepository.findUserById(userId).ifPresent(user ->
        userRepository.deleteUserById(userId)
    );
  }
}