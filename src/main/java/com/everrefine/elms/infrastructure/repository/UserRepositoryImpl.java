package com.everrefine.elms.infrastructure.repository;

import com.everrefine.elms.domain.model.user.EmailAddress;
import com.everrefine.elms.domain.model.user.User;
import com.everrefine.elms.domain.model.user.UserForUpdateRequest;
import com.everrefine.elms.domain.model.user.UserSearchCondition;
import com.everrefine.elms.domain.repository.UserRepository;
import com.everrefine.elms.infrastructure.dao.UserDao;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class UserRepositoryImpl implements UserRepository {

  private final UserDao userDao;

  @Override
  public List<User> findUsersByIds(List<UUID> userIds) {
    if (userIds.isEmpty()) {
      return Collections.emptyList();
    }
    return userDao.findByIdIn(userIds);
  }

  @Override
  public int updateUser(UserForUpdateRequest user) {
    return userDao.updateUserFields(
        user.getId(),
        user.getEmailAddress().getValue(),
        user.getRealName().getValue(), 
        user.getUserName().getValue(),
        user.getThumbnailUrl() != null ? user.getThumbnailUrl().getValue() : null
    );
  }

  @Override
  public User createUser(User user) {
    return userDao.save(user);
  }

  @Override
  public void deleteUserById(UUID id) {
    userDao.deleteById(id);
  }

  @Override
  public Optional<User> findUserById(UUID id) {
    return userDao.findById(id);
  }

  @Override
  public Optional<User> findUserByEmailAddress(EmailAddress emailAddress) {
    return userDao.findByEmailAddress(emailAddress.getValue());
  }

  @Override
  public List<UUID> findUserIdsBySearchConditions(UserSearchCondition userSearchCondition) {
    return userDao.findUserIdsBySearchConditions(
        userSearchCondition.getUserId(),
        userSearchCondition.getUserRole(),
        userSearchCondition.getRealName(),
        userSearchCondition.getUserName(),
        userSearchCondition.getEmailAddress(),
        userSearchCondition.getCreatedDateFrom() != null ? userSearchCondition.getCreatedDateFrom().toString() : null,
        userSearchCondition.getCreatedDateTo() != null ? userSearchCondition.getCreatedDateTo().toString() : null,
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
        userSearchCondition.getCreatedDateFrom() != null ? userSearchCondition.getCreatedDateFrom().toString() : null,
        userSearchCondition.getCreatedDateTo() != null ? userSearchCondition.getCreatedDateTo().toString() : null
    );
  }
}