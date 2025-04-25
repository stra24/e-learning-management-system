package com.everrefine.elms.domain.repository;

import com.everrefine.elms.domain.model.pager.PagerRequest;
import com.everrefine.elms.domain.model.user.EmailAddress;
import com.everrefine.elms.domain.model.user.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

  Optional<User> findUserById(UUID id);

  Optional<User> findUserByEmailAddress(EmailAddress emailAddress);

  List<User> findUsers(PagerRequest pagerRequest);

  int countUsers();
}
