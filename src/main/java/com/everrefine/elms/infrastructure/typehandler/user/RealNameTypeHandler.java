package com.everrefine.elms.infrastructure.typehandler.user;

import com.everrefine.elms.domain.model.user.RealName;
import com.everrefine.elms.infrastructure.typehandler.StringValueObjectTypeHandler;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(RealName.class)
public class RealNameTypeHandler extends StringValueObjectTypeHandler<RealName> {

  public RealNameTypeHandler() {
    super(RealName.class);
  }
}