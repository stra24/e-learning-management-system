package com.everrefine.elms.infrastructure.typehandler.news;

import com.everrefine.elms.domain.model.news.Content;
import com.everrefine.elms.infrastructure.typehandler.StringValueObjectTypeHandler;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(Content.class)
public class ContentTypeHandler extends StringValueObjectTypeHandler<Content> {

  public ContentTypeHandler() {
    super(Content.class);
  }
}