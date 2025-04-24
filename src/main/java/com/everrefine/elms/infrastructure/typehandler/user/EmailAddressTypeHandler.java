package com.everrefine.elms.infrastructure.typehandler.user;

import com.everrefine.elms.domain.model.user.EmailAddress;
import com.everrefine.elms.infrastructure.typehandler.StringValueObjectTypeHandler;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(EmailAddress.class)
public class EmailAddressTypeHandler extends StringValueObjectTypeHandler<EmailAddress> {

  public EmailAddressTypeHandler() {
    super(EmailAddress.class);
  }
}