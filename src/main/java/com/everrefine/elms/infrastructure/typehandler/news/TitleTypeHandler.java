package com.everrefine.elms.infrastructure.typehandler.news;

import com.everrefine.elms.domain.model.news.Title;
import com.everrefine.elms.infrastructure.typehandler.StringValueObjectTypeHandler;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(Title.class)
public class TitleTypeHandler extends StringValueObjectTypeHandler<Title> {

  public TitleTypeHandler() {
    super(Title.class);
  }
}