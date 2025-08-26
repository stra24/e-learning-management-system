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
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserApplicationServiceImpl implements UserApplicationService {

  private final UserRepository userRepository;

  @Override
  public UserDto findUserById(String userId) {
    UUID uuid = UUID.fromString(userId);
    User user = userRepository.findUserById(uuid)
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
    List<UUID> userIds = userRepository.findUserIdsBySearchConditions(userSearchCondition);
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

    LocalDateTime now = LocalDateTime.now();
    User user = new User(
        null,
        new EmailAddress(userCreateCommand.getEmailAddress()),
        Password.encryptAndCreate(userCreateCommand.getPassword()),
        new RealName(userCreateCommand.getRealName()),
        new UserName(userCreateCommand.getUserName()),
        new Url(userCreateCommand.getThumbnailUrl()),
        userCreateCommand.getUserRole(),
        now,
        now
    );
    userRepository.createUser(user);
  }

  @Override
  public void updateUser(UserUpdateCommand userUpdateCommand) {
    UserDto userDto = findUserById(userUpdateCommand.getId().toString());
    UserForUpdateRequest user = new UserForUpdateRequest(
        userDto.getId(),
        new EmailAddress(userUpdateCommand.getEmailAddress()),
        new RealName(userUpdateCommand.getRealName()),
        new UserName(userUpdateCommand.getUserName()),
        new Url(userUpdateCommand.getThumbnailUrl())
    );
    userRepository.updateUser(user);
  }

  @Override
  public void deleteUserById(String userId) {
    UUID uuid = UUID.fromString(userId);
    // ユーザーが存在しなくてもエラーにはしない。
    userRepository.findUserById(uuid).ifPresent(user ->
        userRepository.deleteUserById(uuid)
    );
  }
}