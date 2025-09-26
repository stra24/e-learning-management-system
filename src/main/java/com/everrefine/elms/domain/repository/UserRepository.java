package com.everrefine.elms.domain.repository;

import com.everrefine.elms.domain.model.user.EmailAddress;
import com.everrefine.elms.domain.model.user.User;
import com.everrefine.elms.domain.model.user.UserSearchCondition;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
  Optional<User> findUserById(Integer id);

  List<User> findUsersByIds(List<Integer> ids);

  List<Integer> findUserIdsBySearchConditions(UserSearchCondition userSearchCondition);

  Optional<User> findUserByEmailAddress(EmailAddress emailAddress);

  int countUsers(UserSearchCondition userSearchCondition);

  User createUser(User user);

  User updateUser(User user);

  void deleteUserById(Integer id);
}
