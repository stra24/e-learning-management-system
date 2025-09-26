package com.everrefine.elms.infrastructure.repository;

import com.everrefine.elms.domain.model.user.EmailAddress;
import com.everrefine.elms.domain.model.user.User;
import com.everrefine.elms.domain.model.user.UserSearchCondition;
import com.everrefine.elms.domain.repository.UserRepository;
import com.everrefine.elms.infrastructure.dao.UserDao;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class UserRepositoryImpl implements UserRepository {

  private final UserDao userDao;

  @Override
  public List<User> findUsersByIds(List<Integer> userIds) {
    if (userIds.isEmpty()) {
      return Collections.emptyList();
    }
    return userDao.findByIdIn(userIds);
  }

  @Override
  public User updateUser(User user) {
    return userDao.save(user);
  }

  @Override
  public User createUser(User user) {
    return userDao.save(user);
  }

  @Override
  public void deleteUserById(Integer id) {
    userDao.deleteById(id);
  }

  @Override
  public Optional<User> findUserById(Integer id) {
    return userDao.findById(id);
  }

  @Override
  public Optional<User> findUserByEmailAddress(EmailAddress emailAddress) {
    return userDao.findByEmailAddress(emailAddress.getValue());
  }

  @Override
  public List<Integer> findUserIdsBySearchConditions(UserSearchCondition userSearchCondition) {
    return userDao.findUserIdsBySearchConditions(
        userSearchCondition.getUserId(),
        userSearchCondition.getUserRole(),
        userSearchCondition.getRealName(),
        userSearchCondition.getUserName(),
        userSearchCondition.getEmailAddress(),
        userSearchCondition.getCreatedDateFrom() == null
            ? null
            : userSearchCondition.getCreatedDateFrom(),
        userSearchCondition.getCreatedDateTo() == null
            ? null
            : userSearchCondition.getCreatedDateTo(),
        userSearchCondition.getPageSize(),
        userSearchCondition.getOffset()
    );
  }

  @Override
  public int countUsers(UserSearchCondition userSearchCondition) {
    return userDao.countUsersBySearchConditions(
        userSearchCondition.getUserId(),
        userSearchCondition.getUserRole(),
        userSearchCondition.getRealName(),
        userSearchCondition.getUserName(),
        userSearchCondition.getEmailAddress(),
        userSearchCondition.getCreatedDateFrom() == null
            ? null
            : userSearchCondition.getCreatedDateFrom(),
        userSearchCondition.getCreatedDateTo() == null
            ? null
            : userSearchCondition.getCreatedDateTo()
    );
  }
}