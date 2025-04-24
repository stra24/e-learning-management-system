package com.everrefine.elms.application.service;

import com.everrefine.elms.application.dto.UserDto;
import com.everrefine.elms.application.dto.UserPageDto;
import com.everrefine.elms.domain.model.pager.PagerRequest;
import com.everrefine.elms.domain.model.pager.PagerResponse;
import com.everrefine.elms.domain.model.user.User;
import com.everrefine.elms.domain.repository.UserRepository;
import java.util.List;
import java.util.UUID;
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
    return new UserDto(user);
  }

  @Override
  public UserPageDto findUsers(int pageNum, int pageSize) {
    PagerRequest pagerRequest = new PagerRequest(pageNum, pageSize);
    List<User> users = userRepository.findUsers(pagerRequest);
    int totalSize = userRepository.countUsers();
    PagerResponse pagerResponse = new PagerResponse(pageNum, pageSize, totalSize);
    return new UserPageDto(users, pagerResponse);
  }
}