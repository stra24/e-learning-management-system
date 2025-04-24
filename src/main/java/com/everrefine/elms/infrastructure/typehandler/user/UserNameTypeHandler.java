package com.everrefine.elms.infrastructure.typehandler.user;

import com.everrefine.elms.domain.model.user.UserName;
import com.everrefine.elms.infrastructure.typehandler.StringValueObjectTypeHandler;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(UserName.class)
public class UserNameTypeHandler extends StringValueObjectTypeHandler<UserName> {

  public UserNameTypeHandler() {
    super(UserName.class);
  }
}