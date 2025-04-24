package com.everrefine.elms.infrastructure.typehandler;

import com.everrefine.elms.domain.model.Url;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(Url.class)
public class UrlTypeHandler extends StringValueObjectTypeHandler<Url> {

  public UrlTypeHandler() {
    super(Url.class);
  }
}