package com.everrefine.elms.domain.repository;

import com.everrefine.elms.domain.model.user.EmailAddress;
import com.everrefine.elms.domain.model.user.User;
import com.everrefine.elms.domain.model.user.UserForUpdateRequest;
import com.everrefine.elms.domain.model.user.UserSearchCondition;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.apache.ibatis.annotations.Param;

public interface UserRepository {
  Optional<User> findUserById(UUID id);

  List<User> findUsersByIds(@Param("ids") List<UUID> ids);

  List<UUID> findUserIdsBySearchConditions(UserSearchCondition userSearchCondition);

  Optional<User> findUserByEmailAddress(EmailAddress emailAddress);

  int countUsers(UserSearchCondition userSearchCondition);

  int createUser(User user);

  int updateUser(UserForUpdateRequest user);

  int deleteUserById(UUID id);
}
