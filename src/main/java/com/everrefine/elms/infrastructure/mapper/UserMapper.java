package com.everrefine.elms.infrastructure.mapper;

import com.everrefine.elms.domain.model.pager.PagerRequest;
import com.everrefine.elms.domain.model.user.EmailAddress;
import com.everrefine.elms.domain.model.user.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

  Optional<User> findUserById(UUID id);

  Optional<User> findUserByEmailAddress(EmailAddress emailAddress);

  List<User> findUsers(PagerRequest pagerRequest);

  int countUsers();
}
