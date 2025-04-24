package com.everrefine.elms.infrastructure.typehandler.user;

import com.everrefine.elms.domain.model.user.Password;
import com.everrefine.elms.infrastructure.typehandler.StringValueObjectTypeHandler;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(Password.class)
public class PasswordTypeHandler extends StringValueObjectTypeHandler<Password> {

  public PasswordTypeHandler() {
    super(Password.class);
  }
}