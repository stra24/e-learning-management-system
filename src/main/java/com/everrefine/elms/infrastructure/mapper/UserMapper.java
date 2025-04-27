package com.everrefine.elms.infrastructure.mapper;

import com.everrefine.elms.domain.model.pager.PagerForRequest;
import com.everrefine.elms.domain.model.user.EmailAddress;
import com.everrefine.elms.domain.model.user.User;
import com.everrefine.elms.domain.model.user.UserForUpdateRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

  int updateUser(UserForUpdateRequest user);

  int createUser(User user);

  int deleteUserById(UUID id);

  Optional<User> findUserById(UUID id);

  Optional<User> findUserByEmailAddress(EmailAddress emailAddress);

  List<User> findUsers(PagerForRequest pagerForRequest);

  int countUsers();
}
