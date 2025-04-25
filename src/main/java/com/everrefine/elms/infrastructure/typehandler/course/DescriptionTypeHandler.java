package com.everrefine.elms.infrastructure.typehandler.course;

import com.everrefine.elms.domain.model.course.Description;
import com.everrefine.elms.infrastructure.typehandler.StringValueObjectTypeHandler;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(Description.class)
public class DescriptionTypeHandler extends StringValueObjectTypeHandler<Description> {

  public DescriptionTypeHandler() {
    super(Description.class);
  }
}