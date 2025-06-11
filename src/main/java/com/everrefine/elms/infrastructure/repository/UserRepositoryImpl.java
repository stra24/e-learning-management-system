package com.everrefine.elms.infrastructure.repository;

import com.everrefine.elms.domain.model.PagerForRequest;
import com.everrefine.elms.domain.model.user.EmailAddress;
import com.everrefine.elms.domain.model.user.User;
import com.everrefine.elms.domain.model.user.UserForUpdateRequest;
import com.everrefine.elms.domain.repository.UserRepository;
import com.everrefine.elms.infrastructure.mapper.UserMapper;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class UserRepositoryImpl implements UserRepository {

  private final UserMapper userMapper;

  @Override
  public int updateUser(UserForUpdateRequest user) {
    return userMapper.updateUser(user);
  }

  @Override
  public int createUser(User user) {
    return userMapper.createUser(user);
  }

  @Override
  public int deleteUserById(UUID id) {
    return userMapper.deleteUserById(id);
  }

  @Override
  public Optional<User> findUserById(UUID id) {
    return userMapper.findUserById(id);
  }

  @Override
  public Optional<User> findUserByEmailAddress(EmailAddress emailAddress) {
    return userMapper.findUserByEmailAddress(emailAddress);
  }

  @Override
  public List<User> findUsers(PagerForRequest pagerForRequest) {
    return userMapper.findUsers(pagerForRequest);
  }

  @Override
  public int countUsers() {
    return userMapper.countUsers();
  }
}